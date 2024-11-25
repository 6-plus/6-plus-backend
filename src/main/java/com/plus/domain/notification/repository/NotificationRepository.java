package com.plus.domain.notification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plus.domain.notification.entity.Notification;
import com.plus.domain.notification.enums.DrawNotificationType;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	Optional<Notification> findByDrawIdAndType(Long id, DrawNotificationType type);
}
