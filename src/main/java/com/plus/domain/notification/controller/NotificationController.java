package com.plus.domain.notification.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/connect")
	public ResponseEntity<SseEmitter> connect(@RequestParam Long userId) throws IOException {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(notificationService.connect(userId));
	}

	@PostMapping
	public void test() {
		notificationService.addTask(Draw.builder().build());
	}
}
