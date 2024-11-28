package com.plus.domain.draw.dto.response;

import java.time.LocalDateTime;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.enums.DrawType;

import lombok.Getter;

@Getter
public class DrawSaveResponseDto {
	private Long id;

	private Integer maxWinnerCount;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private LocalDateTime resultTime;

	private DrawType drawType;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Product product;

	public DrawSaveResponseDto(Draw draw) {
		this.id = draw.getId();
		this.maxWinnerCount = draw.getMaxWinnerCount();
		this.startTime = draw.getStartTime();
		this.endTime = draw.getEndTime();
		this.resultTime = draw.getResultTime();
		this.drawType = draw.getDrawType();
		this.createdAt = draw.getCreatedAt();
		this.updatedAt = draw.getUpdatedAt();
		this.product = draw.getProduct();
	}
}
