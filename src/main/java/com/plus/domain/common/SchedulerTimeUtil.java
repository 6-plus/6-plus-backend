package com.plus.domain.common;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.notification.enums.DrawNotificationType;

public class SchedulerTimeUtil {

	public static Instant calculateNotificationTime(Draw draw, DrawNotificationType type) {
		LocalDateTime baseTime = switch (type) {
			case BEFORE_START -> draw.getStartTime();
			case BEFORE_END -> draw.getEndTime();
			default -> throw new IllegalArgumentException("존재하지 않는 알림 타입입니다.");
		};

		LocalDateTime notificationTime = baseTime.minusHours(type.getTime());
		Duration duration = Duration.between(LocalDateTime.now(), notificationTime);

		return Instant.now().plusSeconds(Math.max(0, duration.getSeconds()));
	}

	public static Instant calculate(LocalDateTime localDateTime) {
		Duration duration = Duration.between(LocalDateTime.now(), localDateTime);
		return Instant.now().plusSeconds(Math.max(0, duration.getSeconds()));
	}
}
