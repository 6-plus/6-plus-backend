package com.plus.draw;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.draw.service.DrawService;
import com.plus.domain.user.entity.User;
import com.plus.domain.user.repository.UserRepository;

@SpringBootTest
public class DrawServiceTest {

	@Autowired
	private DrawService drawService;

	@Autowired
	private DrawRepository drawRepository;

	@Autowired
	private UserRepository userRepository;

	private Long drawId;
	private List<Long> userIds; // 생성된 유저 ID 목록

	@BeforeEach
	public void setUp() {
		// 테스트용 Draw 데이터 생성
		Draw draw = new Draw();
		draw.setTotalWinner(10); // 선착순 당첨자 10명
		draw = drawRepository.save(draw);
		drawId = draw.getId(); // 생성된 Draw ID 저장

		// 30명의 테스트 유저 생성 및 저장
		userIds = new ArrayList<>();
		for (int i = 1; i <= 30; i++) {
			User user = new User();
			user.setNickname("user" + i);
			user = userRepository.save(user); // 저장 후 ID 반환
			userIds.add(user.getId());
		}
	}

	@Test
	public void testApplyDrawConcurrently() throws InterruptedException, ExecutionException {
		// 30명의 유저가 동시에 applyDraw()를 호출하는 시나리오
		ExecutorService executorService = Executors.newFixedThreadPool(10); // 10개의 스레드 풀 생성

		// Callable Task 정의
		Callable<String> task = () -> {
			// 각 스레드가 userIds 목록에서 하나의 ID를 사용
			Long userId = userIds.remove(0); // 첫 번째 ID 가져오기
			return drawService.applyDraw(userId, drawId); // 응모 요청
		};

		// 30개의 Callable Task를 실행
		Future<String>[] futures = new Future[30];
		for (int i = 0; i < 30; i++) {
			futures[i] = executorService.submit(task);
		}

		// 결과 검증
		int successCount = 0;
		for (Future<String> future : futures) {
			String result = future.get(); // 각 스레드 결과를 가져옴
			if (result.equals("응모 성공!")) {
				successCount++; // 응모 성공 카운트
			} else if (result.contains("이미 응모한 사용자입니다.")) {
				// 이미 응모한 사용자
			} else {
				fail("Unexpected result: " + result); // 예기치 않은 결과는 실패
			}
		}

		// 성공적으로 응모된 유저 수가 정확히 10명인지 확인
		assertEquals(10, successCount, "응모 성공한 사람이 10명이 아닙니다.");

		executorService.shutdown(); // ExecutorService 종료
	}
}