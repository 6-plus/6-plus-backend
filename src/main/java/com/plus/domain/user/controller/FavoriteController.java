package com.plus.domain.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plus.domain.draw.service.DrawService;
import com.plus.domain.security.UserDetailsImpl;
import com.plus.domain.user.dto.response.FavoriteDeleteResponseDto;
import com.plus.domain.user.dto.response.FavoriteSaveResponseDto;
import com.plus.domain.user.dto.response.FavoriteSearchResponseDto;
import com.plus.domain.user.entity.User;
import com.plus.domain.user.repository.FavoriteRepository;
import com.plus.domain.user.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/draws")
public class FavoriteController {

	private final FavoriteService favoriteService;
	private final FavoriteRepository favoriteRepository;
	private final DrawService drawService;

	@PostMapping("/{drawId}/favorites")
	public ResponseEntity<FavoriteSaveResponseDto> saveFavorite(
		@PathVariable(name = "drawId") Long drawId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(favoriteService.saveFavorite(drawId, userDetails));
	}

	//나의관심응모조회
	@GetMapping("/favorites/my")
	public ResponseEntity<List<FavoriteSearchResponseDto>> searchFavorite(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<FavoriteSearchResponseDto> res = favoriteService.searchfavorite(userDetails);

		return ResponseEntity.ok(res);
	}

	@DeleteMapping("{drawId}/favorites/{favoriteId}")
	public ResponseEntity<FavoriteDeleteResponseDto> deleteFavorite(
		@PathVariable(name = "drawId") Long drawId,
		@PathVariable(name = "favoriteId") Long favoriteId) {
		FavoriteDeleteResponseDto res = favoriteService.deleteFavorite(favoriteId);

		return ResponseEntity.ok(res);
	}

	@GetMapping("/test")
	public ResponseEntity<List<User>> testget() {
		List<User> notificationUserByDrawId = favoriteRepository.findNotificationUserByDrawId(1L);
		return ResponseEntity.ok(notificationUserByDrawId);
	}
}
