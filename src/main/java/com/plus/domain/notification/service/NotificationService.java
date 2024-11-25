package com.plus.domain.notification.service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.plus.domain.common.SchedulerTimeUtil;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.notification.entity.Notification;
import com.plus.domain.notification.entity.SseEmitters;
import com.plus.domain.notification.enums.DrawNotificationType;
import com.plus.domain.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "Notification Service Logger")
@RequiredArgsConstructor
public class NotificationService {

	private final SseEmitters emitters;
	private final TaskScheduler taskScheduler;
	private final NotificationRepository notificationRepository;

	public SseEmitter connect(Long userId) throws IOException {
		return emitters.add(userId);
	}

	@Transactional
	public void addTask(Draw draw) {
		for (DrawNotificationType type : DrawNotificationType.values()) {
			try {
				Instant notificationTime = SchedulerTimeUtil.calculateNotificationTime(draw, type);
				taskScheduler.schedule(() -> sendNotification(draw, type), notificationTime);
				Notification notification = Notification.createWithDraw(draw, type, notificationTime);
				notificationRepository.save(notification);
			} catch (RuntimeException e) {
				log.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
	}

	@Transactional
	public void sendNotification(Draw draw, DrawNotificationType type) {
		try {
			Notification notification = notificationRepository.findByDrawIdAndType(draw.getId(), type)
				.orElseThrow(() -> new IllegalStateException("존재하지 않는 응모입니다."));
			String productName = draw.getProduct().getProductName();
			// TODO: 실제 repository에서 해당 응모를 관심 추천 한 userIds 조회 필요
			List<Long> ids = List.of(1L, 2L);
			emitters.send(ids, productName + type.message);
			notification.complete();
			notificationRepository.save(notification);
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
