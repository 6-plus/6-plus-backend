package com.plus.domain.notification.enums;

import lombok.Getter;

@Getter
public enum DrawNotificationType {
	BEFORE_START(1, " 응모 시작 한시간 전 입니다."),
	BEFORE_END(1, " 응모 종료 한시간 전 입니다.");

	private final Integer time;
	private final String message;

	DrawNotificationType(Integer time, String message) {
		this.time = time;
		this.message = message;
	}

	public String generateMessage(String drawName) {
		return drawName + message;
	}
}
