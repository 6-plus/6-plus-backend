package com.plus.domain.draw.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plus.domain.draw.service.UserDrawService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/draws")
@RequiredArgsConstructor
public class UserDrawController {

	private final UserDrawService userDrawService;

	@PostMapping("/{drawId}/apply")
	public ResponseEntity<String> applyToDraw(@PathVariable Long drawId, @RequestParam Long userId) {
		userDrawService.apply(drawId, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body("응모에 성공하셨습니다.");
	}

}
