package com.plus.domain.user.dto.response;

import java.time.LocalDateTime;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.enums.DrawType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteSearchResponseDto {

	private Long id;
	private Integer maxWinnerCount;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime resultTime;
	private DrawType type;
	private Product product;

	public static FavoriteSearchResponseDto from(Draw draw) {
		return FavoriteSearchResponseDto.builder()
			.id(draw.getId())
			.maxWinnerCount(draw.getMaxWinnerCount())
			.startTime(draw.getStartTime())
			.endTime(draw.getEndTime())
			.resultTime(draw.getResultTime())
			.type(draw.getDrawType())
			.product(draw.getProduct())
			.build();
	}
}
