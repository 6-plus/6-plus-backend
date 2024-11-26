package com.plus.domain.user.dto.response;

import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.enums.DrawType;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class FavoriteSearchResponseDto {

    private Long id;
    private Integer totalWinners;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime resultTime;
    private DrawType type;
    private Product product;

}
