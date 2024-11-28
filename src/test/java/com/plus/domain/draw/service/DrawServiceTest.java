package com.plus.domain.draw.service;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.plus.domain.draw.dto.response.EntryResultResponseDto;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.draw.repository.UserDrawRepository;
import com.plus.lock.service.LockService;

@SpringBootTest
class DrawServiceTest {

	@Autowired
	LockService lockService;

	@Autowired
	DrawService drawService;

	@Autowired
	DrawRepository drawRepository;

	@Autowired
	UserDrawRepository userDrawRepository;

	@Test
	public void testConcurrentLocking() throws InterruptedException {
		//given
		String lockKey = "testLock";
		Draw draw = Draw.builder()
			.maxWinnerCount(10)
			.build();
		Draw savedDraw = drawRepository.save(draw);
		int timeoutSeconds = 2;

		ExecutorService executorService = Executors.newFixedThreadPool(30);
		AtomicLong userId = new AtomicLong();

		//when
		for (int i = 0; i < 30; i++) {
			executorService.submit(() -> {
				EntryResultResponseDto result = lockService.process(
					lockKey,
					timeoutSeconds,
					() -> drawService.entry(savedDraw.getId(), userId.getAndIncrement())
				);

				System.out.println(userId + " : " + result.getStatus().getMessage());
			});

		}

		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.SECONDS);

		//then
		long count = userDrawRepository.count();
		assertThat(count).isEqualTo(10);
	}
}