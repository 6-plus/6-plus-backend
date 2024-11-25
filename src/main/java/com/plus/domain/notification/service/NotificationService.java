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
import com.plus.domain.draw.entity.Product;
import com.plus.domain.notification.entity.SseEmitters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "Notification Service Logger")
@RequiredArgsConstructor
public class NotificationService {

	private final SseEmitters emitters;
	private final TaskScheduler taskScheduler;
	private static final Integer ONE_HOUR = 1;

	public SseEmitter connect(Long userId) throws IOException {
		return emitters.add(userId);
	}

	@Transactional
	public void addTask(Draw draw) {
		Product product = draw.getProduct();
		Instant beforeStartNoticeTime = SchedulerTimeUtil.calculateMinusHour(draw.getStartTime(), ONE_HOUR);
		Instant beforeEndNoticeTime = SchedulerTimeUtil.calculateMinusHour(draw.getEndTime(), ONE_HOUR);
		Instant drawTime = SchedulerTimeUtil.calculate(draw.getResultTime());
		taskScheduler.schedule(() -> sendNotificationBeforeDrawStart(product), beforeStartNoticeTime);
		taskScheduler.schedule(() -> sendNotificationBeforeDrawEnd(product), beforeEndNoticeTime);
		taskScheduler.schedule(() -> sendNotificationToWinner(product), drawTime);
	}

	public void sendNotificationBeforeDrawStart(Product product) {
		/*
		TODO: 응모 시작 한시간 전 로직
		List<Long> ids = List.of() => 제거예정
		List<Long> ids = favoriteDrawRepository.findByDrawId(drawId).stream()
			.map(favoriteDraw -> favoriteDraw.getUserId())
			.toList();
		*/
		List<Long> ids = List.of(1L, 2L);
		emitters.send(ids, product.getProductName() + "의 응모 시작 한시간 전 입니다.");
	}

	public void sendNotificationBeforeDrawEnd(Product product) {
		/*
		TODO: 응모 종료 한시간 전 로직
		List<Long> ids = List.of() => 제거예정
		List<Long> ids = favoriteDrawRepository.findByDrawId(drawId).stream()
			.map(favoriteDraw -> favoriteDraw.getUserId())
			.toList();
		*/
		List<Long> ids = List.of(1L, 2L);
		emitters.send(ids, product.getProductName() + "의 응모 종료 한시간 전 입니다.");
	}

	public void sendNotificationToWinner(Product product) {
		// TODO: 당첨자 알림
		List<Long> ids = List.of(3L);
		emitters.send(ids, product.getProductName() + "에 당첨되셨습니다!");
	}
}
