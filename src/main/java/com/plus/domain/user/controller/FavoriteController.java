package com.plus.domain.user.controller;

import com.plus.domain.user.dto.FavoriteSaveResponseDto;
import com.plus.domain.user.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/draws")
public class FavoriteController {

    private final FavoriteService favoriteService;

    //TODO : Favorite C,R,D
    //관심응모 생성
    @PostMapping("/{drawId}/favorites")
    public FavoriteSaveResponseDto saveFavorite(@PathVariable(name = "drawId") Long drawrId) {
        String email = "testUser1@gmail.com";

        return favoriteService.saveFavorite(drawrId, email);
    }

    //관심응모 조회

    //관심응모 삭제
}
