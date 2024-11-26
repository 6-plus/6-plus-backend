package com.plus.domain.user.dto.response;

import com.plus.domain.user.entity.Favorite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteSaveResponseDto {

	private String message;

	public static FavoriteSaveResponseDto from(Favorite savedFavorite) {
		return FavoriteSaveResponseDto.builder()
			.message("관심응모로 등록 되었습니다.")
			.build();
	}
}
