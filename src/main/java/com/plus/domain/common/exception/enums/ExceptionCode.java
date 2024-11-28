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
	FILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "업로드할 파일이 존재하지 않습니다."),
	DRAW_NOT_FOUND(HttpStatus.BAD_REQUEST, "응모가 존재하지 않습니다."),
	FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 삭제할 수 없습니다."),
	INVALID_FILE_URL(HttpStatus.BAD_REQUEST, "파일을 삭제할 수 없습니다."),
	MAXIMUM_PARTICIPANTS_REACHED(HttpStatus.BAD_REQUEST, "응모 인원 초과."),
	LOCK_ACQUISITION_FAILED(HttpStatus.BAD_REQUEST, "락 획득 실피."),
	INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST, "서버 에러."),
	ALREADY_APPLIED(HttpStatus.BAD_REQUEST, "이미 응모한 사용자."),

	// Auth Exception
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
	DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
	NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "Token not found."),

	// Review Exception

	// User Exception
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다."),
	NICKNAME_ALREADY_IN_USE(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

	// Favorite Exception
	DRAW_NOT_FOUND_OF_FAVORITE(HttpStatus.NOT_FOUND, "요청한 응모를 찾을 수 없습니다.."),
	DUPLICATE_FAVORITE(HttpStatus.NOT_FOUND, "이미 저장된 관심 응모입니다.");

	private final HttpStatus code;
	private final String message;
}
