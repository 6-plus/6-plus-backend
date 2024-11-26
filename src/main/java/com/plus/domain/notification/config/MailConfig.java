package com.plus.domain.notification.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
@RequiredArgsConstructor
public class MailConfig {

	private final MailProperties mailProperties;

	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(mailProperties.getHost());
		javaMailSender.setUsername(mailProperties.getUsername());
		javaMailSender.setPassword(mailProperties.getPassword());
		javaMailSender.setPort(mailProperties.getPort());

		Properties properties = javaMailSender.getJavaMailProperties();
		properties.putAll(mailProperties.getProperties());

		javaMailSender.setJavaMailProperties(properties);
		javaMailSender.setDefaultEncoding("UTF-8");

		return javaMailSender;
	}
}