package com.plus.domain.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plus.domain.review.dto.request.ReviewSaveRequestDto;
import com.plus.domain.review.dto.response.ReviewSaveResponseDto;
import com.plus.domain.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping("/draws/{drawId}")
	public ResponseEntity<ReviewSaveResponseDto> saveReview(
		@RequestBody ReviewSaveRequestDto requestDto,
		@PathVariable(name = "drawId") Long drawId,
		@RequestParam Long userId
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(reviewService.saveReview(requestDto, drawId, userId));
	}
}
