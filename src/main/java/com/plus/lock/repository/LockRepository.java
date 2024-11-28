package com.plus.lock.repository;

public interface LockRepository {
	Long getLock(String key, int timeoutSeconds);

	void releaseLock(String key);
}
