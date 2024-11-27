package com.plus.domain.draw.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_draw_lock")
public class UserDrawLock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lock_id")
	private Long lockId; // AUTO_INCREMENT 기본 키

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt; // 생성 시점, 자동 설정

	@Column(name = "expires_at", nullable = false)
	private LocalDateTime expiresAt; // 만료 시점
}