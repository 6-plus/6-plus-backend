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
	NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없습니다. 관리자만 응모를 생성할 수 있습니다."),
	DUPLICATE_DRAW_NAME(HttpStatus.CONFLICT, "이미 등록된 응모 이름입니다."),
	INVALID_TOTAL_WINNERS(HttpStatus.BAD_REQUEST, "당첨자 수는 1명 이상이어야 합니다."),
	INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "시작일과 종료일이 올바르지 않습니다."),
	FILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "업로드할 파일이 존재하지 않습니다."),

	// Auth Exception

	// Review Exception

	// User Exception

	// Favorite Exception
	
	;
	private final HttpStatus code;
	private final String message;
}
