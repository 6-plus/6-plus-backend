package com.plus.domain.draw.entity;

import java.time.LocalDateTime;

import com.plus.domain.common.BaseTimestamped;
import com.plus.domain.draw.enums.DrawType;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Draw extends BaseTimestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer maxWinnerCount;
	private Integer currentWinnerCount;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime resultTime;
	@Enumerated(EnumType.STRING)
	private DrawType drawType;
	@Embedded
	private Product product;

	@Builder
	public Draw(
		Long id,
		Integer maxWinnerCount,
		LocalDateTime startTime,
		LocalDateTime endTime,
		LocalDateTime resultTime,
		DrawType drawType,
		Product product
	) {
		this.id = id;
		this.maxWinnerCount = maxWinnerCount;
		this.startTime = startTime;
		this.endTime = endTime;
		this.resultTime = resultTime;
		this.drawType = drawType;
		this.product = product;
	}

	public boolean isDrawClosed() {
		return this.maxWinnerCount <= this.currentWinnerCount;
	}

	public void increaseCurrentWinnerCount() {
		this.currentWinnerCount++;
	}
}
