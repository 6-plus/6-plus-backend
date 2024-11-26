package com.plus.domain.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus.domain.draw.repository.UserDrawRepository;
import com.plus.domain.review.dto.request.ReviewSaveRequestDto;
import com.plus.domain.review.dto.response.ReviewSaveResponseDto;
import com.plus.domain.review.dto.response.ReviewSearchResponseDto;
import com.plus.domain.review.entity.Review;
import com.plus.domain.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserDrawRepository userDrawRepository;

	public ReviewSaveResponseDto saveReview(ReviewSaveRequestDto requestDto, Long drawId, Long userId) {
		if (!userDrawRepository.existsByUserIdAndDrawId(userId, drawId)) {
			throw new IllegalArgumentException("당첨된 응모의 리뷰만 작성할 수 있습니다.");
		}

		String contents = requestDto.getContents();
		String image = requestDto.getImage();
		Review review = reviewRepository.save(
			Review.builder().contents(contents).image(image).userId(userId).drawId(drawId).build()
		);
		return new ReviewSaveResponseDto(review);
	}

	@Transactional(readOnly = true)
	public Page<ReviewSearchResponseDto> searchDrawReviews(Long drawId, Pageable pageable) {
		return reviewRepository.findAllByDrawId(drawId, pageable).map(ReviewSearchResponseDto::new);
	}
}
