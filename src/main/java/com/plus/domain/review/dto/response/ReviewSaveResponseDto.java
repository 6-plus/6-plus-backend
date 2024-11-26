package com.plus.domain.review.dto.response;

import java.time.LocalDateTime;

import com.plus.domain.review.entity.Review;

public record ReviewSaveResponseDto(
	Long id,
	String contents,
	String image,
	Long userId,
	Long drawId,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public ReviewSaveResponseDto(Review review) {
		this(
			review.getId(),
			review.getContents(),
			review.getImage(),
			review.getUserId(),
			review.getDrawId(),
			review.getCreatedAt(),
			review.getUpdatedAt()
		);
	}
}
