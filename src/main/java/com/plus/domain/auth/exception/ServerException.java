package com.plus.domain.auth.exception;

public class ServerException extends RuntimeException {
	// 기본 생성자
	public ServerException(String message) {
		super(message);  // 부모 클래스의 생성자 호출
	}
}