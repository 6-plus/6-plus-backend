package com.plus.domain.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExceptionResponse {
	private String message;
	private HttpStatus code;

	@Builder
	public ExceptionResponse(String message, HttpStatus code) {
		this.message = message;
		this.code = code;
	}

	public static ExceptionResponse of(ExpectedException expectedException) {
		return ExceptionResponse.builder()
			.code(expectedException.getExceptionCode())
			.message(expectedException.getExceptionMessage())
			.build();
	}
}
