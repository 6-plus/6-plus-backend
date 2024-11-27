package com.plus.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plus.domain.security.UserDetailsImpl;
import com.plus.domain.user.dto.request.UserDeleteRequestDto;
import com.plus.domain.user.dto.request.UserUpdateRequestDto;
import com.plus.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@PatchMapping
	public ResponseEntity<String> updateUserInfo(@Valid @RequestBody UserUpdateRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		userService.updateUserInfo(requestDto, userDetails);
		return ResponseEntity.status(HttpStatus.OK).body("회원 정보가 수정되었습니다.");
	}

	@DeleteMapping("/signout")
	public ResponseEntity<String> deleteUser(@RequestBody UserDeleteRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		userService.deleteUser(requestDto, userDetails);
		return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴가 완료되었습니다.");

	}
}
