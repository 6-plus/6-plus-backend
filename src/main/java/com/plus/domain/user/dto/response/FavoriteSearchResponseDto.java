package com.plus.domain.user.dto.response;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.enums.DrawType;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class FavoriteSearchResponseDto {

    private Long id;
    private Integer totalWinners;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime resultTime;
    private DrawType type;
    private Product product;

    public static FavoriteSearchResponseDto from(Draw draw) {
        return FavoriteSearchResponseDto.builder()
                .id(draw.getId())
                .totalWinners(draw.getTotalWinner())
                .startTime(draw.getStartTime())
                .endTime(draw.getEndTime())
                .resultTime(draw.getResultTime())
                .type(draw.getDrawType())
                .product(draw.getProduct())
                .build();
    }
}
