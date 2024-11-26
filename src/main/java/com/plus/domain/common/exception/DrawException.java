package com.plus.domain.common.exception;

import com.plus.domain.common.exception.enums.ExceptionCode;

public class DrawException extends ExpectedException{
    public DrawException(ExceptionCode exceptionCode) {super(exceptionCode);}
}
