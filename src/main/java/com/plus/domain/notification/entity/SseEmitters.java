package com.plus.domain.notification.entity;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j(topic = "SseEmitters Logger")
public class SseEmitters {

	Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
	private static Long DEFAULT_TIMEOUT = 60 * 1000L * 30; // 30분 동안 연결 지속

	public SseEmitter add(Long userId) throws IOException {
		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
		emitters.put(userId, emitter);
		log.info("new emitter added : {}", emitter);
		log.info("emitter list size : {}", emitters.size());
		this.setEmitterConfig(userId);
		try {
			emitter.send(SseEmitter.event()
				.name("connect")
				.data("connected!"));
		} catch (IOException e) {
			throw new IOException(e);
		}
		return emitter;
	}

	private void setEmitterConfig(Long userId) {
		SseEmitter emitter = emitters.get(userId);
		emitter.onCompletion(() -> {
			log.info("onCompletion callback");
			emitters.remove(userId);
		});
		emitter.onTimeout(() -> {
			log.info("onTimeout callback");
			emitter.complete();
		});
	}
}
