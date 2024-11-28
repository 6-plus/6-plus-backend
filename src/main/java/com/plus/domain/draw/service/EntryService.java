package com.plus.domain.draw.service;


import com.plus.domain.draw.dto.response.EntrySaveResponseDto;
import com.plus.domain.draw.entity.UserDraw;
import com.plus.domain.draw.entity.UserDrawLock;
import com.plus.domain.draw.repository.UserDrawLcokRepository;
import com.plus.domain.draw.repository.UserDrawRepository;
import com.plus.domain.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EntryService {


	private final UserDrawRepository userDrawRepository;
	private final UserDrawLcokRepository userDrawLcokRepository;


	@Transactional
	public EntrySaveResponseDto saveEntry(Long drawId, UserDetailsImpl userDetails) {

		final int MAX_RETRY_TIME_MS = 5000; // 최대 대기 시간: 5초
		final int RETRY_INTERVAL_MS = 100; // 재시도 간격: 100ms

		long startTime = System.currentTimeMillis();

		// 락 확인 및 재시도
		while (true) {
			Optional<UserDrawLock> byId = userDrawLcokRepository.findById(drawId);
			if (byId.isEmpty()) {
				break; // 락이 없으면 작업 진행
			}

			if (System.currentTimeMillis() - startTime > MAX_RETRY_TIME_MS) {
				throw new RuntimeException("락 해제가 5초를 초과하였습니다. 요청이 실패했습니다.");
			}

			try {
				Thread.sleep(RETRY_INTERVAL_MS); // 잠시 대기 후 재시도
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // 인터럽트 상태 복구
				throw new RuntimeException("재시도 중 인터럽트가 발생하였습니다.", e);
			}
		}

		// 락 생성
		userDrawLcokRepository.saveAndFlush(UserDrawLock.builder()
			.lockId(drawId)
			.createdAt(LocalDateTime.now())
			.build());

		// 유효성 검사(남은 응모 갯수)
		long count = userDrawRepository.count();
		if (count > 3) {
			throw new RuntimeException("남은 응모 갯수를 초과하였습니다."); // 예외 처리
		}


		Long userId = userDetails.getUser().getId();
		UserDraw saveEntry = userDrawRepository.save(UserDraw.builder()
			.drawId(drawId)
			.userId(userId)
			.build());

		// 락 해제
		userDrawLcokRepository.deleteAll();


		return EntrySaveResponseDto.from(saveEntry);
	}
}
