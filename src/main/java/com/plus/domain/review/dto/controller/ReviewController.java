package com.plus.domain.review.dto.controller;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.plus.domain.review.dto.request.ReviewSaveRequestDto;
import com.plus.domain.review.dto.request.ReviewUpdateRequestDto;
import com.plus.domain.review.dto.response.ReviewSaveResponseDto;
import com.plus.domain.review.dto.response.ReviewSearchResponseDto;
import com.plus.domain.review.dto.response.ReviewUpdateResponseDto;
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

	@GetMapping("/my")
	public ResponseEntity<Page<ReviewSearchResponseDto>> searchMyReviews(
		@RequestParam Long userId,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(reviewService.searchMyReviews(userId, pageable));
	}

	@PatchMapping("/{reviewId}")
	public ResponseEntity<ReviewUpdateResponseDto> updateReview(
		@RequestParam Long userId,
		@PathVariable(name = "reviewId") Long reviewId,
		@RequestBody ReviewUpdateRequestDto requestDto
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(reviewService.updateReview(userId, reviewId, requestDto));
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<String> deleteReview(
		@RequestParam Long userId,
		@PathVariable(name = "reviewId") Long reviewId
	) {
		reviewService.deleteReview(userId, reviewId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body("당첨 리뷰가 삭제 되었습니다.");
	}
}
