package com.plus.domain.common.exception;

import com.plus.domain.common.exception.enums.ExceptionCode;

public class FavoriteException extends ExpectedException {
	public FavoriteException(ExceptionCode exceptionCode) {
		super(exceptionCode);
	}
}
