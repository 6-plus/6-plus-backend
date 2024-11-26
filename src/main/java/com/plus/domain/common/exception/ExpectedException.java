package com.plus.domain.common.exception;

import org.springframework.http.HttpStatus;

import com.plus.domain.common.exception.enums.ExceptionCode;

public class ExpectedException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public ExpectedException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getExceptionMessage() {
		return exceptionCode.getMessage();
	}

	public HttpStatus getExceptionCode() {
		return exceptionCode.getCode();
	}
}
