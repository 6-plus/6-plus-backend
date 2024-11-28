package com.plus.domain.rabbitMq;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RabbitMqController {
	private final RabbitMqService rabbitMqService;

	@PostMapping("/api/auth/drawing/{drawId}")
	public ResponseEntity<String> saveUserDraw(
		@RequestParam(name = "userId") Long userId,
		@PathVariable Long drawId
	) throws InterruptedException {
		rabbitMqService.saveUserDraw(UserDrawSaveReqDto.builder().userId(userId).drawId(drawId).build());
		boolean isWin = rabbitMqService.AmIWin(userId, drawId);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(userId + "has " + isWin);
	}
}
