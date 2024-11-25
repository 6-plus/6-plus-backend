package com.plus.domain.draw.repository;

import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.entity.QUserDraw;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static com.plus.domain.draw.entity.QDraw.draw;
import static com.plus.domain.draw.entity.QUserDraw.userDraw;
import static com.plus.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserDrawRepositoryQueryImpl implements UserDrawRepositoryQuery {
	private final JPAQueryFactory jPAQueryFactory;

	@Override
	public Page<DrawSearchResponseDto> findAllDrawByUserId(Long userId, Pageable pageable) {
		QUserDraw drawUser = new QUserDraw("drawUser");
		var drawSearches = jPAQueryFactory
			.select(Projections.constructor(
				DrawSearchResponseDto.class,
				draw.id, draw.totalWinner, user.countDistinct(), draw.startTime, draw.endTime, draw.resultTime,
				draw.drawType, draw.product
			))
			.from(userDraw)
			.where(userDraw.userId.eq(userId))
			.leftJoin(draw).on(userDraw.drawId.eq(draw.id))
			.leftJoin(drawUser).on(draw.id.eq(drawUser.drawId))
			.leftJoin(user).on(drawUser.userId.eq(user.id))
			.groupBy(draw.id)
			.orderBy(draw.endTime.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return PageableExecutionUtils.getPage(drawSearches, pageable, drawSearches::size);
	}
}
