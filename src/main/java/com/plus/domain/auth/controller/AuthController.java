package com.plus.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plus.domain.auth.dto.SigninRequestDto;
import com.plus.domain.auth.dto.SignupRequestDto;
import com.plus.domain.auth.service.AuthService;
import com.plus.domain.security.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final JwtUtil jwtUtil;
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
		authService.signup(signupRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공적으로 완료되었습니다.");
	}

	@PostMapping("/signin")
	public ResponseEntity<Void> signin(@Valid @RequestBody SigninRequestDto signinRequestDto,
		HttpServletResponse response) {
		// 인증 후 토큰 생성
		String token = authService.signin(signinRequestDto);

		// 쿠키 생성
		Cookie cookie = jwtUtil.createCookie(token);

		// 응답에 쿠키 추가
		response.addCookie(cookie);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

}