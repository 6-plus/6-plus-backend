package com.plus.domain.user.service;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.user.dto.FavoriteSaveResponseDto;
import com.plus.domain.user.entity.Favorite;
import com.plus.domain.user.entity.User;
import com.plus.domain.user.repository.FavoriteRepository;
import com.plus.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final DrawRepository drawRepository;
    private final UserRepository userRepository;

    @Transactional
    public FavoriteSaveResponseDto saveFavorite(Long drawId, String email) {
        Draw draw = drawRepository.findById(drawId)
                .orElseThrow(() -> new IllegalArgumentException("Draw not found with ID: " + drawId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        Favorite savedFavorite = favoriteRepository.save(Favorite.builder()
                .drawId(draw.getId())
                .userId(user.getId())
                .build());

        return savedFavorite.toDto();
    }


//    //관심 응모 삭제 sercive package
//    public FavoriteDeleteResponseDto deleteFavorite(Long favoriteId) {
//        favoriteRepository.deleteById(favoriteId);
//        return null;
//
//
//    }
}
