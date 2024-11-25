package com.plus.domain.notification.controller;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.plus.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;
	private final AtomicLong userIds = new AtomicLong();

	@GetMapping("/connect")
	public ResponseEntity<SseEmitter> connect() throws IOException {
		// TODO: 추후 AuthUser에 존재하는 실제 userId로 교체 예정
		long userId = userIds.getAndIncrement();
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(notificationService.connect(userId));
	}
}
