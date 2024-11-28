package com.plus.domain.draw.dto.response;

import com.plus.domain.draw.entity.UserDraw;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrySaveResponseDto {

	private Long id;
	private Long drawId;
	private Long userId;
	private String message;


	public static EntrySaveResponseDto from(UserDraw saveEntry) {
		return EntrySaveResponseDto.builder()
			.message(".")
			.drawId(saveEntry.getDrawId())
			.userId(saveEntry.getUserId())
			.build();
	}

}
