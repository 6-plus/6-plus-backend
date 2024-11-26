package com.plus.domain.review.controller;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.plus.domain.review.dto.request.ReviewSaveRequestDto;
import com.plus.domain.review.dto.request.ReviewUpdateRequestDto;
import com.plus.domain.review.dto.response.ReviewSaveResponseDto;
import com.plus.domain.review.dto.response.ReviewSearchResponseDto;
import com.plus.domain.review.dto.response.ReviewUpdateResponseDto;
import com.plus.domain.review.service.ReviewService;
import com.plus.domain.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping("/draws/{drawId}")
	public ResponseEntity<ReviewSaveResponseDto> saveReview(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable(name = "drawId") Long drawId,
		@RequestBody ReviewSaveRequestDto requestDto
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(reviewService.saveReview(userDetails.getUser().getId(), drawId, requestDto));
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
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(reviewService.searchMyReviews(userDetails.getUser().getId(), pageable));
	}

	@PatchMapping("/{reviewId}")
	public ResponseEntity<ReviewUpdateResponseDto> updateReview(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable(name = "reviewId") Long reviewId,
		@RequestBody ReviewUpdateRequestDto requestDto
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(reviewService.updateReview(userDetails.getUser().getId(), reviewId, requestDto));
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<String> deleteReview(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable(name = "reviewId") Long reviewId
	) {
		reviewService.deleteReview(userDetails.getUser().getId(), reviewId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body("당첨 리뷰가 삭제 되었습니다.");
	}
}
