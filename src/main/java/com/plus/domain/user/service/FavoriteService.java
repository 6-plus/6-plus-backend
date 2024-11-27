package com.plus.domain.user.service;

import static com.plus.domain.common.exception.enums.ExceptionCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus.domain.common.exception.FavoriteException;
import com.plus.domain.common.exception.UserException;
import com.plus.domain.common.exception.enums.ExceptionCode;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.security.UserDetailsImpl;
import com.plus.domain.user.dto.response.FavoriteDeleteResponseDto;
import com.plus.domain.user.dto.response.FavoriteSaveResponseDto;
import com.plus.domain.user.dto.response.FavoriteSearchResponseDto;
import com.plus.domain.user.dto.response.NotificationUserDto;
import com.plus.domain.user.entity.Favorite;
import com.plus.domain.user.repository.FavoriteRepository;
import com.plus.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final DrawRepository drawRepository;
	private final UserRepository userRepository;

	@Transactional
	public FavoriteSaveResponseDto saveFavorite(Long drawId, UserDetailsImpl userDetails) {
		Long userId = userDetails.getUser().getId();

		if (!drawRepository.existsById(drawId)) {
			throw new FavoriteException(DRAW_NOT_FOUND_OF_FAVORITE);
		}

		if (!userRepository.existsById(userId)) {
			throw new UserException(USER_NOT_FOUND);
		}

		if (favoriteRepository.existsByUserIdAndDrawId(userId, drawId)) {
			throw new FavoriteException(ExceptionCode.DUPLICATE_FAVORITE);
		}

		Favorite savedFavorite = favoriteRepository.save(Favorite.builder()
			.drawId(drawId)
			.userId(userId)
			.build());

		return FavoriteSaveResponseDto.from(savedFavorite);
	}

	@Transactional(readOnly = true)
	public List<FavoriteSearchResponseDto> searchfavorite(UserDetailsImpl userDetails) {

		List<Favorite> res = favoriteRepository.findAllByUserId(userDetails.getUser().getId());

		List<FavoriteSearchResponseDto> responseDtos = new ArrayList<>();
		for (Favorite re : res) {
			Draw draw = drawRepository.findById(re.getDrawId()).get();
			responseDtos.add(FavoriteSearchResponseDto.from(draw));
		}

		return responseDtos;
	}

	@Transactional
	public FavoriteDeleteResponseDto deleteFavorite(Long favoriteId) {
		favoriteRepository.deleteById(favoriteId);
		return FavoriteDeleteResponseDto.builder()
			.message("관심응모가 삭제 되었습니다.")
			.build();
	}

	public List<NotificationUserDto> findUsersByDrawId(Long drawId) {
		return favoriteRepository.findNotificationUserByDrawId(drawId).stream()
			.map(NotificationUserDto::from)
			.toList();
	}
}
