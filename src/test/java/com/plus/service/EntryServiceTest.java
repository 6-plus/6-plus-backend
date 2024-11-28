package com.plus.service;

import com.plus.domain.draw.dto.response.EntrySaveResponseDto;
import com.plus.domain.draw.entity.UserDraw;
import com.plus.domain.draw.repository.UserDrawLcokRepository;
import com.plus.domain.draw.repository.UserDrawRepository;
import com.plus.domain.draw.service.EntryService;
import com.plus.domain.security.UserDetailsImpl;
import com.plus.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class EntryServiceTest {

	@Autowired
	private EntryService entryService;

	@Autowired
	private UserDrawLcokRepository userDrawLockRepository;

	@Autowired
	private UserDrawRepository userDrawRepository;

	@Test
	void testSaveEntry_Success() {
		// given
		Long drawId = 1L;
		UserDetailsImpl userDetails = createMockUserDetails(1L); // Mock UserDetails 생성

		// when
		EntrySaveResponseDto response = entryService.saveEntry(drawId, userDetails);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getDrawId()).isEqualTo(drawId);

		// DB 락 확인
		assertThat(userDrawLockRepository.findAll()).isEmpty();
	}

	@Test
	void testSaveEntry_ExceedsEntryLimit() {
		// given
		Long drawId = 1L;
		UserDetailsImpl userDetails = createMockUserDetails(1L);

		// 유효성 검사 실패 상황 시뮬레이션
		for (int i = 0; i < 3; i++) {
			userDrawRepository.save(UserDraw.builder()
				.drawId(drawId)
				.userId(userDetails.getUser().getId())
				.build());
		}

		// when / then
		assertThrows(RuntimeException.class, () -> entryService.saveEntry(drawId, userDetails));
	}

	@Test
	void testSaveEntry_Concurrency() throws InterruptedException {
		// given
		int threadCount = 10;
		Long drawId = 1L;
		CountDownLatch latch = new CountDownLatch(threadCount);//스레드가 모두 종료될 때까지 대기
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

		// Lock 초기화
		userDrawLockRepository.deleteAll();

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					UserDetailsImpl userDetails = createMockUserDetails((long)(Math.random() * 1000)); // 랜덤 User 생성
					entryService.saveEntry(drawId, userDetails);
				} catch (Exception e) {
					System.err.println("Exception in thread: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(); // 모든 스레드 종료 대기
		executorService.shutdown();

		// then
		long savedEntries = userDrawRepository.count();
		assertThat(savedEntries).isLessThanOrEqualTo(3); // 응모 제한 확인
	}

	// Mock UserDetails 생성
	private UserDetailsImpl createMockUserDetails(Long userId) {
		User user = User.builder().id(userId).nickname("testUser" + userId).build();
		return new UserDetailsImpl(user);
	}
}
