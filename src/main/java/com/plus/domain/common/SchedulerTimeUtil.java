package com.plus.domain.common;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class SchedulerTimeUtil {

	public static Instant calculateMinusHour(LocalDateTime localDateTime, int hour) {
		LocalDateTime notificationTime = localDateTime.minusHours(hour);
		Duration duration = Duration.between(LocalDateTime.now(), notificationTime);
		return Instant.now().plusSeconds(Math.max(0, duration.getSeconds()));
	}

	public static Instant calculate(LocalDateTime localDateTime) {
		Duration duration = Duration.between(LocalDateTime.now(), localDateTime);
		return Instant.now().plusSeconds(Math.max(0, duration.getSeconds()));
	}
}
