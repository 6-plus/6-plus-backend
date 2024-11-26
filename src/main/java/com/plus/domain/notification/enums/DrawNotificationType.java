package com.plus.domain.notification.enums;

public enum DrawNotificationType {
	BEFORE_START(1, " 응모 시작 한시간 전 입니다."),
	BEFORE_END(1, " 응모 종료 한시간 전 입니다.");

	public final Integer time;
	public final String message;

	DrawNotificationType(Integer time, String message) {
		this.time = time;
		this.message = message;
	}
}
