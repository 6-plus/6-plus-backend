package com.plus.domain.rabbitMq;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDrawSaveReqDto {
	private Long userId;
	private Long drawId;

	@Builder
	public UserDrawSaveReqDto(Long userId, Long drawId) {
		this.userId = userId;
		this.drawId = drawId;
	}
}
