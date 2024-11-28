package com.plus.domain.rabbitMq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.draw.repository.UserDrawRepository;
import com.plus.domain.user.entity.User;
import com.plus.domain.user.repository.UserRepository;

@SpringBootTest
class RabbitMqServiceTest {

	@Autowired
	private DrawRepository drawRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserDrawRepository userDrawRepository;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Test
	@Transactional
	@Rollback(false)
	void test() throws InterruptedException {
		//given
		int userCount = 30;

		Draw draw = Draw.builder().totalWinner(10).build();
		draw = drawRepository.save(draw);
		Long drawId = draw.getId();

		List<UserDrawSaveReqDto> userDrawSaveReqDtoList = new ArrayList<>();
		for(int i = 0; i < userCount; i++){
			User user = User.builder().nickname("user" + i).build();
			user = userRepository.save(user);
			userDrawSaveReqDtoList.add(UserDrawSaveReqDto.builder().userId(user.getId()).drawId(drawId).build());
		}

		ExecutorService executorsService = Executors.newFixedThreadPool(userCount);
		RabbitMqService rabbitMqService = new RabbitMqService(userDrawRepository, drawRepository, rabbitTemplate);
		for (UserDrawSaveReqDto userDrawSaveReqDto : userDrawSaveReqDtoList) {
			executorsService.submit(() -> rabbitMqService.saveUserDraw(userDrawSaveReqDto));
			// Thread.sleep(1);
		}
		Thread.sleep(1000);
	}
}
