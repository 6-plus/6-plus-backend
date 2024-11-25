package com.plus.domain.notification.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.plus.domain.notification.entity.SseEmitters;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final SseEmitters emitters;

	public SseEmitter connect(Long userId) throws IOException {
		return emitters.add(userId);
	}
}
