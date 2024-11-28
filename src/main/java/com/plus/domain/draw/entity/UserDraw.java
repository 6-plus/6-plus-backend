package com.plus.domain.draw.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserDraw {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private Long drawId;
	private boolean win;
	@CreatedDate
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@Builder
	public UserDraw(Long id, Long userId, Long drawId, boolean win, LocalDateTime createdAt) {
		this.id = id;
		this.userId = userId;
		this.drawId = drawId;
		this.win = win;
		this.createdAt = createdAt;
	}

	public static UserDraw createSuccessUserDraw(Long drawId, Long userId) {
		return UserDraw.builder()
			.drawId(drawId)
			.userId(userId)
			.win(true)
			.build();
	}
}
