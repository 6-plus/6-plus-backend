package com.plus.draw;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.draw.repository.UserDrawRepository;
import com.plus.domain.draw.service.DrawService;

@SpringBootTest
class DrawServiceTest {

	@Autowired
	DrawService drawService;

	@Autowired
	DrawRepository drawRepository;

	@Autowired
	UserDrawRepository userDrawRepository;

	@Test
	public void 테스트() throws InterruptedException {
		// given
		int memberCount = 30;
		Draw draw = Draw.builder()
			.totalWinner(10)
			.build();
		Draw saved = drawRepository.save(draw);

		ExecutorService executorService = Executors.newFixedThreadPool(30);
		CountDownLatch latch = new CountDownLatch(memberCount);

		AtomicInteger successCount = new AtomicInteger();
		AtomicLong userId = new AtomicLong(61);

		// when
		for (int i = 0; i < memberCount; i++) {
			executorService.submit(() -> {
				try {
					drawService.applyDraw(userId.getAndIncrement(), saved.getId());
					successCount.incrementAndGet();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		System.out.println("successCount = " + successCount);

		// then
		long successDrawCount = userDrawRepository.count();
		assertThat(successDrawCount)
			.isEqualTo(10);
	}
}