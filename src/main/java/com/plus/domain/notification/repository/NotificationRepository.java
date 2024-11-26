package com.plus.domain.notification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plus.domain.notification.entity.Notification;
import com.plus.domain.notification.enums.DrawNotificationType;
import com.plus.domain.notification.enums.NotificationStatus;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	Optional<Notification> findByDrawIdAndType(Long id, DrawNotificationType type);

	List<Notification> findAllByStatus(NotificationStatus notificationStatus);
}
