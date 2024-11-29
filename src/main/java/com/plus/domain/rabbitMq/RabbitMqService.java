package com.plus.domain.rabbitMq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus.domain.common.exception.DrawException;
import com.plus.domain.common.exception.enums.ExceptionCode;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.UserDraw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.draw.repository.UserDrawRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RabbitMqService {

	private final UserDrawRepository userDrawRepository;
	private final DrawRepository drawRepository;

	@Value("${rabbitmq.queue.name}")
	private String queueName;

	@Value("${rabbitmq.exchange.name}")
	private String exchangeName;

	@Value("${rabbitmq.routing.key}")
	private String routingKey;

	private final RabbitTemplate rabbitTemplate;

	public void saveUserDraw(UserDrawSaveReqDto reqDto) {
		rabbitTemplate.convertAndSend(exchangeName, routingKey, reqDto);
		log.info("UserDraw send: {}, {}", reqDto.getUserId(), reqDto.getDrawId());
	}

	@RabbitListener(queues = "${rabbitmq.queue.name}")
	@Transactional
	public void receiveMessage(UserDrawSaveReqDto reqDto) {
		Draw draw = drawRepository.findById(reqDto.getDrawId())
			.orElseThrow(() -> new DrawException(ExceptionCode.DRAW_NOT_FOUND));
		UserDraw userDraw = userDrawRepository.save(
			UserDraw.builder().userId(reqDto.getUserId()).drawId(reqDto.getDrawId()).build());

		draw.counting();
		int applicantCount = draw.getApplicant();

		if (applicantCount <= draw.getTotalWinner()) {
			userDraw.win();
		}
		log.info("Received UserDraw : {}, {}, {}", userDraw.getUserId(), userDraw.getDrawId(),
			userDraw.isWin() ? "win" : "loose");
	}

	public boolean AmIWin(Long userId, Long drawId) throws InterruptedException {
		Thread.sleep(100);
		UserDraw userDraw = userDrawRepository.findByUserIdAndDrawId(userId, drawId).orElse(null);
		if(userDraw == null) return false;
		return userDraw.isWin();
	}
}
