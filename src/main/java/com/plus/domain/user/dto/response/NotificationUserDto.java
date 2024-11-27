package com.plus.domain.user.dto.response;

import com.plus.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NotificationUserDto {
	private final Long id;
	private final String email;

	@Builder
	public NotificationUserDto(Long id, String email) {
		this.id = id;
		this.email = email;
	}

	public static NotificationUserDto from(User user) {
		return NotificationUserDto.builder()
			.id(user.getId())
			.email(user.getEmail())
			.build();
	}
}
