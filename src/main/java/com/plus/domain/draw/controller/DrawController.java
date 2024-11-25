package com.plus.domain.draw.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plus.domain.draw.dto.request.DrawSearchRequestDto;
import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.service.DrawService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/draws")
@RequiredArgsConstructor
public class DrawController {

	private final DrawService drawService;

	@GetMapping("/my")
	public ResponseEntity<List<DrawSearchResponseDto>> searchMyDraw(
		@RequestParam Long userId,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(drawService.searchMyDraw(userId, page, size));
	}

	@GetMapping
	public ResponseEntity<List<DrawSearchResponseDto>> searchDraws(
		@RequestBody DrawSearchRequestDto requestDto,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(drawService.searchDraws(requestDto, page, size));
	}
}
