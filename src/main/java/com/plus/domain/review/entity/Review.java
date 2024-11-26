package com.plus.domain.review.entity;

import com.plus.domain.common.BaseTimestamped;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Review extends BaseTimestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String contents;
	private String image;
	private Long userId;
	private Long drawId;

	@Builder
	public Review(Long id, String contents, String image, Long userId, Long drawId) {
		this.id = id;
		this.contents = contents;
		this.image = image;
		this.userId = userId;
		this.drawId = drawId;
	}
}
