package com.plus.domain.user.controller;

import com.plus.domain.draw.service.DrawService;
import com.plus.domain.user.dto.response.FavoriteDeleteResponseDto;
import com.plus.domain.user.dto.response.FavoriteSaveResponseDto;
import com.plus.domain.user.dto.response.FavoriteSearchResponseDto;
import com.plus.domain.user.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/draws")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final DrawService drawService;


    @PostMapping("/{drawId}/favorites")
    public ResponseEntity<FavoriteSaveResponseDto> saveFavorite(
            @PathVariable(name = "drawId") Long drawId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(favoriteService.saveFavorite(drawId, 1L));  // TODO : 나중에 토큰에서 가져와서 넣을 예정
    }

    //나의관심응모조회
    @GetMapping("/favorites/my")
    public ResponseEntity<List<FavoriteSearchResponseDto>> searchFavorite(
            @RequestParam Long userId) {

        List<FavoriteSearchResponseDto> res = favoriteService.searchfavorite(userId);

        return ResponseEntity.ok(res);
    }


    @DeleteMapping("{drawId}/favorites/{favoriteId}")
    public ResponseEntity<FavoriteDeleteResponseDto> deleteFavorite(
            @PathVariable(name = "drawId") Long drawId,
            @PathVariable(name = "favoriteId") Long favoriteId) {
        FavoriteDeleteResponseDto res = favoriteService.deleteFavorite(favoriteId);

        return ResponseEntity.ok(res);
    }
}
