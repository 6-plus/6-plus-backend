package com.plus.lock.service;

import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.plus.lock.repository.LockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LockService {

	private final LockRepository lockRepository;

	public <T> T process(String userLockName, int timeoutSeconds, Supplier<T> supplier) {
		try {
			lockRepository.getLock(userLockName, timeoutSeconds);
			return supplier.get();
		} finally {
			lockRepository.releaseLock(userLockName);
		}
	}
}
