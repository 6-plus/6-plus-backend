package com.plus.domain.user.controller;

import com.plus.domain.user.dto.FavoriteSaveResponseDto;
import com.plus.domain.user.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<FavoriteSaveResponseDto> saveFavorite(@PathVariable(name = "drawId") Long drawrId) {
        String email = "testUser1@gmail.com";//test용 입니다. 유저기능완료 후 삭제 얘정
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(favoriteService.saveFavorite(drawrId, email));
    }

//    //관심응모 조회
//
//    public ResponseEntity<FavoriteSearchResponseDto> searchFavorite(
//            @PathVariable(name = "drawId") Long drawId,
//            @PathVariable(name = "favoriteId") Long favoriteId) {
//        return null;
//    }
//
//
//    //관심응모 삭제
//    @DeleteMapping("{drawId}/favorites/[favoriteId]")
//    public ResponseEntity<FavoriteDeleteResponseDto> deleteFavorite(
//            @PathVariable(name = "drawId") Long drawId,
//            @PathVariable(name = "favoriteId") Long favoriteId) {
//        return null;
//
//    }
}
