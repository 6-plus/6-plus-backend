package com.plus.domain.common.exception;

import com.plus.domain.common.exception.enums.ExceptionCode;

public class NotificationException extends ExpectedException {
	public NotificationException(ExceptionCode exceptionCode) {
		super(exceptionCode);
	}
}
