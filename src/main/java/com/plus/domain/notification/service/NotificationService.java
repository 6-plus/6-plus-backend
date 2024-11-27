package com.plus.domain.notification.service;

import static com.plus.domain.common.exception.enums.ExceptionCode.*;

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
import com.plus.domain.common.exception.NotificationException;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.notification.entity.Notification;
import com.plus.domain.notification.entity.SseEmitters;
import com.plus.domain.notification.enums.DrawNotificationType;
import com.plus.domain.notification.enums.NotificationStatus;
import com.plus.domain.notification.repository.NotificationRepository;
import com.plus.domain.user.dto.response.NotificationUserDto;
import com.plus.domain.user.service.FavoriteService;

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
	private final FavoriteService favoriteService;
	private final MailService mailService;

	public SseEmitter connect(Long userId) throws IOException {
		return emitters.add(userId);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initScheduledNotification() {
		List<Notification> notifications = notificationRepository.findAllByStatus(NotificationStatus.PENDING);
		notifications.forEach(notification -> {
			Draw draw = drawRepository.findById(notification.getDrawId())
				.orElseThrow(() -> new NotificationException(NOT_FOUND_NOTIFICATION));
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
				.orElseThrow(() -> new NotificationException(NOT_FOUND_NOTIFICATION));
			String productName = draw.getProduct().getProductName();
			List<NotificationUserDto> notificationUsers = favoriteService.findUsersByDrawId(draw.getId());
			List<Long> userIds = notificationUsers.stream()
				.map(NotificationUserDto::getId)
				.toList();
			List<String> userEmails = notificationUsers.stream()
				.map(NotificationUserDto::getEmail)
				.toList();
			emitters.send(userIds, productName + type.generateMessage(productName));
			mailService.sendEmailToUsers(userEmails, notification, productName);
			notification.complete();
			notificationRepository.save(notification);
		} catch (RuntimeException e) {
			log.error("알림 전송 중 오류 발생 : {}", e.getMessage());
			throw new NotificationException(SSE_CONNECTION_ERROR);
		}
	}
}
