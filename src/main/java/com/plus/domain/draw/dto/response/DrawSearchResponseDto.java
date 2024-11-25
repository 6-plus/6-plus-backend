package com.plus.domain.draw.dto.response;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.enums.DrawType;

import java.time.LocalDateTime;

public record DrawSearchResponseDto(
        Long id,
        Integer totalWinners,
        Long applicants,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime resultTime,
        DrawType type,
        Product product
) {
}
