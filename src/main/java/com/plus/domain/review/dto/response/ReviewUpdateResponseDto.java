package com.plus.domain.review.dto.response;

import java.time.LocalDateTime;

import com.plus.domain.review.entity.Review;

public record ReviewUpdateResponseDto(
	Long id,
	String contents,
	String image,
	Long userId,
	Long drawId,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public ReviewUpdateResponseDto(Review review) {
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
