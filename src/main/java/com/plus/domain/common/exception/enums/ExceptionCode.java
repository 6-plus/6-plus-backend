package com.plus.domain.common.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
	// Notification Exception
	NOT_FOUND_NOTIFICATION(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다."),
	SSE_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SSE 연결 중 오류 발생"),
	// Draw Exception

	// Auth Exception

	// Review Exception

	// User Exception

	// Favorite Exception
	
	;
	private final HttpStatus code;
	private final String message;
}
