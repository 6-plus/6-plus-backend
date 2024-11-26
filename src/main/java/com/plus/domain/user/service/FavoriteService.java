package com.plus.domain.user.service;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.user.dto.response.FavoriteDeleteResponseDto;
import com.plus.domain.user.dto.response.FavoriteSaveResponseDto;
import com.plus.domain.user.dto.response.FavoriteSearchResponseDto;
import com.plus.domain.user.entity.Favorite;
import com.plus.domain.user.entity.User;
import com.plus.domain.user.repository.FavoriteRepository;
import com.plus.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final DrawRepository drawRepository;
    private final UserRepository userRepository;

    @Transactional
    public FavoriteSaveResponseDto saveFavorite(Long drawId, Long userId) {
        Draw draw = drawRepository.findById(drawId)
                .orElseThrow(() -> new IllegalArgumentException("Draw not found with ID: " + drawId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Favorite savedFavorite = favoriteRepository.save(Favorite.builder()
                .drawId(draw.getId())
                .userId(user.getId())
                .build());

        return savedFavorite.toDto();
    }

    @Transactional(readOnly = true)
    public List<FavoriteSearchResponseDto> searchfavorite(Long userId) {

        List<Favorite> res = favoriteRepository.findAllByUserId(userId);

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
}
