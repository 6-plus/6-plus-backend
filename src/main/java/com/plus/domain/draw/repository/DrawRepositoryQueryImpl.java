package com.plus.domain.draw.repository;

import static com.plus.domain.draw.entity.QDraw.*;
import static com.plus.domain.draw.entity.QUserDraw.*;
import static com.plus.domain.user.entity.QUser.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.entity.QUserDraw;
import com.plus.domain.draw.enums.DrawStatus;
import com.plus.domain.draw.enums.SortBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DrawRepositoryQueryImpl implements DrawRepositoryQuery {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<DrawSearchResponseDto> findAllDrawByUserId(Long userId, int page, int size) {
		return findQuery(page, size)
			.where(userDraw.userId.eq(userId))
			.orderBy(draw.endTime.asc())
			.fetch();
	}

	@Override
	public List<DrawSearchResponseDto> findAllDraws(String productName, int page, int size, DrawStatus status,
		LocalDateTime now, SortBy sortBy) {
		return findQuery(page, size)
			.where(
				productNameEq(productName),
				drawStatusEq(status, now)
			)
			.orderBy(getOrderSpecifier(sortBy))
			.fetch();
	}

	private JPAQuery<DrawSearchResponseDto> findQuery(int page, int size) {
		QUserDraw drawUser = new QUserDraw("drawUser");
		return jpaQueryFactory
			.select(Projections.constructor(
				DrawSearchResponseDto.class,
				draw.id, draw.totalWinner, user.countDistinct(), draw.startTime, draw.endTime, draw.resultTime,
				draw.drawType, draw.product
			))
			.from(userDraw)
			.leftJoin(draw).on(userDraw.drawId.eq(draw.id))
			.leftJoin(drawUser).on(draw.id.eq(drawUser.drawId))
			.leftJoin(user).on(drawUser.userId.eq(user.id))
			.groupBy(draw.id)
			.offset((long)(page - 1) * size)
			.limit(size);
	}


	private BooleanExpression productNameEq(String productName) {
		return Objects.nonNull(productName) ? draw.product.productName.eq(productName) : null;
	}

	private BooleanExpression drawStatusEq(DrawStatus drawStatus, LocalDateTime now) {
		return switch (drawStatus) {
			case BEFORE_DRAW -> draw.startTime.after(now);
			case DRAWING -> draw.startTime.before(now).and(draw.endTime.after(now));
			case AFTER_DRAW -> draw.endTime.before(now);
		};
	}

	private OrderSpecifier<?> getOrderSpecifier(SortBy sortBy) {
		return switch (sortBy) {
			case END_TIME_DEC -> draw.endTime.desc();
			case TOTAL_WINNERS_DEC -> draw.totalWinner.desc();
			case TOTAL_WINNERS_ASC -> draw.totalWinner.asc();
			case APPLICANTS_DEC -> user.countDistinct().desc();
			case APPLICANTS_ASC -> user.countDistinct().asc();
		};
	}
}
