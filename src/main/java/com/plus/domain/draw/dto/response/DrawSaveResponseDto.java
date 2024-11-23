package com.plus.domain.draw.dto.response;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.enums.DrawType;
import lombok.*;

import java.time.LocalDateTime;
@Getter
public class DrawSaveResponseDto {
    private Long id;

    private Integer totalWinner;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime resultTime;

    private DrawType drawType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Product product;

    @Builder
    public DrawSaveResponseDto(Long id,
                               Integer totalWinner,
                               LocalDateTime startTime,
                               LocalDateTime endTime,
                               LocalDateTime resultTime,
                               DrawType drawType,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt,
                               Product product) {
        this.id = id;
        this.totalWinner = totalWinner;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultTime = resultTime;
        this.drawType = drawType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.product = product;
    }
}
