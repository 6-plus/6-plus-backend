package com.plus.domain.draw.controller;

import com.plus.domain.draw.dto.response.DrawSearchResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.plus.domain.draw.service.DrawService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/draws")
@RequiredArgsConstructor
public class DrawController {

	private final DrawService drawService;

	@GetMapping("/my")
	public ResponseEntity<Page<DrawSearchResponseDto>> searchMyDraw(
		@RequestParam Long userId,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(drawService.searchMyDraw(userId, pageable));
	}
}
