package com.plus.domain.notification.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.plus.domain.notification.service.NotificationService;
import com.plus.domain.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/connect")
	public ResponseEntity<SseEmitter> connect(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) throws IOException {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(notificationService.connect(userDetails.getUser().getId()));
	}
}
