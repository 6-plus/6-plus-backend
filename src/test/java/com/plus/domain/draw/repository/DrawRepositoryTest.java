package com.plus.domain.draw.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.plus.domain.draw.Exception.AlreadyAppliedException;
import com.plus.domain.draw.Exception.ApplicationFullException;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.service.UserDrawService;

import jakarta.transaction.Transactional;

@SpringBootTest
public class DrawRepositoryTest {

	@Autowired
	private DrawRepository drawRepository;
	@Autowired
	private UserDrawService userDrawService;

	@Autowired
	private UserDrawRepository userDrawRepository;

	@Test
	@Transactional
	public void testConcurrentApply() throws InterruptedException {
		// Given: Draw 객체 생성 및 저장
		Draw draw = Draw.builder()
			.totalWinner(10) // 최대 당첨자 수 10명
			.currentApplicants(0) // 현재 응모자 수 0명
			.build();
		Draw savedDraw = drawRepository.save(draw);

		int numberOfThreads = 1;
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		AtomicInteger successCount = new AtomicInteger(0);
		AtomicLong userId = new AtomicLong();
		// When: 30개의 스레드를 실행하여 응모 처리
		for (int i = 0; i < numberOfThreads; i++) {

			executorService.submit(() -> {
				try {
					userDrawService.apply(savedDraw.getId(), userId.incrementAndGet());
					successCount.incrementAndGet();  // 응모 성공한 경우 카운트 증가
				} catch (ApplicationFullException | AlreadyAppliedException e) {
					// 응모 실패한 경우는 예외를 던지므로 성공 카운트 증가하지 않음
				}
			});
		}

		// 모든 스레드가 종료될 때까지 기다림
		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.SECONDS);

		// Then: 10명만 성공적으로 응모되어야 한다.
		assertThat(successCount.get()).isEqualTo(10);

		// 추가로, 응모 정보가 정상적으로 저장되었는지 확인
		long appliedCount = userDrawRepository.count();
		assertThat(appliedCount).isEqualTo(10);  // 응모한 사용자 수가 10명이어야 함
	}
}
