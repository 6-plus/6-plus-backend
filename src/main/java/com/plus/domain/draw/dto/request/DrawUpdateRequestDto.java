package com.plus.domain.draw.dto.request;

import java.time.LocalDateTime;

import org.antlr.v4.runtime.misc.NotNull;

import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.enums.DrawType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrawUpdateRequestDto {

	@NotNull
	private Integer totalWinners; //당첨 인원

	@NotNull
	private LocalDateTime startTime;

	@NotNull
	private LocalDateTime endTime;

	private LocalDateTime resultTime;

	@NotNull
	private DrawType drawType; // Enum: FIRST_COME or PERIOD

	@NotNull
	private Product product;

	@Builder
	public DrawUpdateRequestDto(
		Integer totalWinners,
		LocalDateTime startTime,
		LocalDateTime endTime,
		DrawType drawType,
		Product product) {
		this.totalWinners = totalWinners;
		this.startTime = startTime;
		this.endTime = endTime;
		this.drawType = drawType;
		this.product = product;
	}
}
