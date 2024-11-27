package com.plus.domain.common.exception;

import com.plus.domain.common.exception.enums.ExceptionCode;

public class UserException extends ExpectedException {
	public UserException(ExceptionCode exceptionCode) {
		super(exceptionCode);
	}
}