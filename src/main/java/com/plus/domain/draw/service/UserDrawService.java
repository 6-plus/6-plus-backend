package com.plus.domain.draw.service;

import org.springframework.stereotype.Service;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.UserDraw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.draw.repository.UserDrawRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDrawService {

	private final UserDrawRepository userDrawRepository;
	private final DrawRepository drawRepository;

	public void saveUserDraw(Long userId, Long drawId) {
		Draw draw = drawRepository.findById(drawId).orElseThrow();
		UserDraw userDraw = userDrawRepository.save(UserDraw.builder().userId(userId).drawId(drawId).build());
		if(userDraw.getUserId() < draw.getTotalWinner()) userDraw.win();
	}
}

/*
 객체 생성하고서 결과 받기
saveUserDraw 메서드를 만들고, 생성된 객체의 id 값을 반환.
해당 반환 값이 Draw의 winnerCount보다 작으면 성공.
마지막에 요청한 클라이언트는 객체가 모두 생성되는 시간을 기다려야 한다.
=> 알아서 생성하게 두고, 생성될 결과만 미리 확인?

 생성 api 요청만 날리고 결과 예상하기
saveUserDraw는 당연히 해야할 것이고, 결과를 따로 예측하는 로직으로 결과 미리 반환.
현재 userDraw의 수를 기록한 값과 예정 작업의 수를 리턴.
입력 받을 때마다 갯수를 기록하기만 하면 될 듯.
각각의 클라이언트는 갯수 세는 로직만 기다리고 리턴 받기를 기다리는 방식.

 갯수 세는 api와 생성하는 메서드는 따로 분리
갯수 세는 api는 모든 클라이언트가 동기
생성 메서드는 갯수 세는 api 에게서 요청 받으면 순서대로 생성.

 근데 생성하다 실패하면 id 값이랑 카운팅은 증가하는데?
=> 마지막 일부만 최종 UserDraw 생성의 성공 여부 확인하고 리턴?
=> 실패할 요청은 아예 DB에 접근할 수 없도록
=> 현재 구조상 실패할 요청이 없기에 고려하지 않아도 된다

 각각의 draw 를 구분하고 속도를 빠르게 하기 위해서 현재까지 생성을 요청한 수를 drawId로 map으로 관리.
 */

/*
 메세지 큐
MOM, 메세지 지향 미들웨어
2개 사이의 메세지 전달을 해주는 매개체
서비스의 큐에 명령어 넣고, scheduled 로 주기적으로 큐에서 뽑아서 실행.
응모 요청하고 sleep 하다가 응모 결과 나오면 interrupt 로 catch
 */
