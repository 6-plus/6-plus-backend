package com.plus.domain.draw.dto.response;

import com.plus.domain.draw.entity.UserDraw;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrySaveResponseDto {

	private String message;

	public static EntrySaveResponseDto from(UserDraw saveEntry) {
		return EntrySaveResponseDto.builder()
			.message(".")
			.build();
	}
}
