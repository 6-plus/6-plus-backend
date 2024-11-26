package com.plus.domain.review.service;

import org.springframework.stereotype.Service;

import com.plus.domain.review.dto.request.ReviewSaveRequestDto;
import com.plus.domain.review.dto.response.ReviewSaveResponseDto;
import com.plus.domain.review.entity.Review;
import com.plus.domain.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;

	public ReviewSaveResponseDto saveReview(ReviewSaveRequestDto requestDto, Long drawId, Long userId) {
		String contents = requestDto.getContents();
		String image = requestDto.getImage();
		Review review = reviewRepository.save(
			Review.builder().contents(contents).image(image).userId(userId).drawId(drawId).build()
		);
		return new ReviewSaveResponseDto(review);
	}
}
