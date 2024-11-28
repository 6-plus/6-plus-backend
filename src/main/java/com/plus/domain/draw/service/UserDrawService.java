package com.plus.domain.draw.service;

import org.springframework.stereotype.Service;

import com.plus.domain.draw.Exception.AlreadyAppliedException;
import com.plus.domain.draw.Exception.ApplicationFullException;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.UserDraw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.draw.repository.UserDrawRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDrawService {

	private final UserDrawRepository userDrawRepository;
	private final DrawRepository drawRepository;

	@Transactional
	public void apply(Long drawId, Long userId) {
		try {
			// 1. 해당 Draw에 대해 이미 응모했는지 확인
			log.info("사용자 {}가 응모하려는 Draw ID: {}에 대한 응모 여부 확인 중...", userId, drawId);
			boolean alreadyApplied = userDrawRepository.existsByDrawIdAndUserId(drawId, userId);
			if (alreadyApplied) {
				log.warn("사용자 {}는 이미 Draw ID: {}에 응모한 상태입니다. 중복 응모 시도.", userId, drawId);
				throw new AlreadyAppliedException("이미 응모하셨습니다. 중복 응모는 불가합니다.");
			}

			// 2. 해당 Draw의 현재 응모 인원 및 최대 인원 확인
			log.info("Draw ID: {}의 응모 정보를 조회 중...", drawId);
			Draw existingDraw = drawRepository.findById(drawId)
				.orElseThrow(() -> {
					log.error("Draw ID: {}에 해당하는 응모가 존재하지 않습니다.", drawId);
					return new IllegalArgumentException("해당 응모가 존재하지 않습니다.");
				});

			log.info("Draw ID: {}의 현재 응모 인원: {}, 최대 응모 인원: {}", drawId, existingDraw.getCurrentApplicants(),
				existingDraw.getTotalWinner());
			if (existingDraw.getCurrentApplicants() >= existingDraw.getTotalWinner()) {
				log.warn("Draw ID: {}의 응모 인원이 마감되었습니다.", drawId);
				throw new ApplicationFullException("응모 인원이 마감되었습니다.");
			}

			// 3. 새로운 응모 생성
			log.info("사용자 {}가 Draw ID: {}에 응모를 생성합니다. 당첨 상태로 설정.", userId, drawId);
			UserDraw userDraw = new UserDraw();
			userDraw.setUserId(userId);
			userDraw.setDrawId(drawId);
			userDraw.setWin(true); // 바로 당첨
			userDrawRepository.save(userDraw); // 커밋시점에 락 해제
			log.info("사용자 {}의 응모가 성공적으로 저장되었습니다. 응모 ID: {}", userId, userDraw.getId());

			// 4. 응모 후 Draw의 응모 인원 업데이트
			log.info("Draw ID: {}의 응모 인원을 1명 증가시킵니다. 이전 응모 인원: {}", drawId, existingDraw.getCurrentApplicants());
			existingDraw.setCurrentApplicants(existingDraw.getCurrentApplicants() + 1);
			drawRepository.save(existingDraw); // Draw 정보 업데이트
			log.info("Draw ID: {}의 응모 인원이 업데이트되었습니다. 새로운 응모 인원: {}", drawId, existingDraw.getCurrentApplicants());

		} catch (AlreadyAppliedException | ApplicationFullException e) {
			log.error("응모 처리 중 예외 발생: {}", e.getMessage());
			throw e; // 예외를 다시 던져서 트랜잭션 롤백 및 예외 처리
		} catch (Exception e) {
			log.error("응모 처리 중 예기치 못한 예외 발생: {}", e.getMessage(), e);
			throw new RuntimeException("응모 처리 중 예기치 못한 오류가 발생했습니다.", e);
		}
	}
}