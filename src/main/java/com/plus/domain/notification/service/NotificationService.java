package com.plus.domain.notification.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.plus.domain.common.SchedulerTimeUtil;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.notification.entity.Notification;
import com.plus.domain.notification.entity.SseEmitters;
import com.plus.domain.notification.enums.DrawNotificationType;
import com.plus.domain.notification.enums.NotificationStatus;
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
	private final DrawRepository drawRepository;

	public SseEmitter connect(Long userId) throws IOException {
		return emitters.add(userId);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initScheduledNotification() {
		List<Notification> notifications = notificationRepository.findAllByStatus(NotificationStatus.PENDING);
		notifications.forEach(notification -> {
			Draw draw = drawRepository.findById(notification.getDrawId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 응모입니다."));
			if (LocalDateTime.now().isBefore(notification.getNotificationTime())) {
				Instant notificationTime = SchedulerTimeUtil.calculateNotificationTime(draw, notification.getType());
				taskScheduler.schedule(() -> sendNotification(draw, notification.getType()), notificationTime);
			}
			if (LocalDateTime.now().isAfter(notification.getNotificationTime())) {
				notification.complete();
			}
			notificationRepository.save(notification);
		});
	}

	@Transactional
	public void addTask(Draw draw) {
		for (DrawNotificationType type : DrawNotificationType.values()) {
			Instant notificationTime = SchedulerTimeUtil.calculateNotificationTime(draw, type);
			taskScheduler.schedule(() -> sendNotification(draw, type), notificationTime);
			Notification notification = Notification.createWithDraw(draw, type, notificationTime);
			notificationRepository.save(notification);
		}
	}

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
			log.error("알림 전송 중 오류 발생 : {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
