package com.plus.domain.common.exception;

import com.plus.domain.common.exception.enums.ExceptionCode;

public class AuthException extends ExpectedException {
	public AuthException(ExceptionCode exceptionCode) {
		super(exceptionCode);
	}
}
