package com.plus.domain.user.entity;

import com.plus.domain.user.dto.FavoriteSaveResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorite_draw")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long drawId;

    public FavoriteSaveResponseDto toDto() {
        return FavoriteSaveResponseDto.builder()
                .message("관심응모로 등록 되었습니다.")
                .build();
    }
}
