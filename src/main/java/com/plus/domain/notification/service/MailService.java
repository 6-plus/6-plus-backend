package com.plus.domain.notification.service;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus.domain.notification.entity.Notification;
import com.plus.domain.notification.enums.DrawNotificationType;
import com.plus.domain.user.dto.response.NotificationUserDto;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Mail Service Logger")
@Transactional(readOnly = true)
public class MailService {

	private final JavaMailSender javaMailSender;

	public void sendEmailNotice(String email, String contents) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject("관심 응모 알림");
			mimeMessageHelper.setText(contents);
			javaMailSender.send(mimeMessage);
			log.info("Succeeded to send Email");
		} catch (Exception e) {
			log.error("Failed to send Email");
			throw new RuntimeException(e);
		}
	}

	public void sendEmailToUsers(
		List<NotificationUserDto> notificationUsers,
		Notification notification,
		String productName
	) {
		DrawNotificationType type = notification.getType();
		List<String> emails = notificationUsers.stream()
			.map(NotificationUserDto::getEmail)
			.toList();

		emails.forEach(email -> {
			sendEmailNotice(email, type.generateMessage(productName));
		});
	}
}
