package com.plus.domain.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExpectedException.class)
	public ResponseEntity<ExceptionResponse> handleExpectedException(ExpectedException exception) {
		ExceptionResponse response = ExceptionResponse.of(exception);
		return ResponseEntity
			.status(response.getCode())
			.body(response);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ExceptionResponse> handleUnExpectedException(RuntimeException exception) {
		ExceptionResponse response = ExceptionResponse.builder()
			.code(HttpStatus.INTERNAL_SERVER_ERROR)
			.message(exception.getMessage())
			.build();
		return ResponseEntity
			.status(response.getCode())
			.body(response);
	}
}

