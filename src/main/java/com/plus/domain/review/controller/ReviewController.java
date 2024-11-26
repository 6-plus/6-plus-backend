package com.plus.domain.review.controller;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.plus.domain.review.dto.request.ReviewSaveRequestDto;
import com.plus.domain.review.dto.response.ReviewSaveResponseDto;
import com.plus.domain.review.dto.response.ReviewSearchResponseDto;
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

	@GetMapping("/draws/{drawId}")
	public ResponseEntity<Page<ReviewSearchResponseDto>> searchDrawReviews(
		@PathVariable(name = "drawId") Long drawId,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(reviewService.searchDrawReviews(drawId, pageable));
	}
}
