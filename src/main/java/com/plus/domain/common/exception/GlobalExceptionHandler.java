package com.plus.domain.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// @ExceptionHandler(InvalidRequestException.class)
	// public ResponseEntity<Map<String, Object>> invalidRequestExceptionException(InvalidRequestException ex) {
	// 	HttpStatus status = HttpStatus.BAD_REQUEST;
	// 	return getErrorResponse(status, ex.getMessage());
	// }

	public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("status", status.name());
		errorResponse.put("code", status.value());
		errorResponse.put("message", message);

		return new ResponseEntity<>(errorResponse, status);
	}
}

