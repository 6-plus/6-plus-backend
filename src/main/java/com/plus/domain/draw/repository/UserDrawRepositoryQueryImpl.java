package com.plus.domain.draw.repository;

import static com.plus.domain.draw.entity.QDraw.*;
import static com.plus.domain.draw.entity.QUserDraw.*;
import static com.plus.domain.user.entity.QUser.*;

import java.util.List;

import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.entity.QUserDraw;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDrawRepositoryQueryImpl implements UserDrawRepositoryQuery {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<DrawSearchResponseDto> findAllDrawByUserId(Long userId, int page, int size) {
		return findQuery(page, size)
			.where(userDraw.userId.eq(userId))
			.orderBy(draw.endTime.asc())
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
}
