package com.plus.domain.notification.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.notification.enums.DrawNotificationType;
import com.plus.domain.notification.enums.NotificationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long drawId;
	private LocalDateTime notificationTime;
	@Enumerated(EnumType.STRING)
	private NotificationStatus status;
	@Enumerated(EnumType.STRING)
	private DrawNotificationType type;

	@Builder
	public Notification(Long id, Long drawId, LocalDateTime notificationTime, NotificationStatus status,
		DrawNotificationType type) {
		this.id = id;
		this.drawId = drawId;
		this.notificationTime = notificationTime;
		this.status = status;
		this.type = type;
	}

	public static Notification createWithDraw(Draw draw, DrawNotificationType type, Instant notificationTime) {
		return Notification.builder()
			.drawId(draw.getId())
			.status(NotificationStatus.PENDING)
			.notificationTime(LocalDateTime.ofInstant(notificationTime, ZoneId.of("Asia/Seoul")))
			.type(type)
			.build();
	}

	public void complete() {
		this.status = NotificationStatus.COMPLETE;
	}
}
