package com.plus.domain.draw.entity;

import com.plus.domain.common.BaseTimestamped;
import com.plus.domain.draw.enums.DrawType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Draw extends BaseTimestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer totalWinner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime resultTime;
    @Enumerated(EnumType.STRING)
    private DrawType drawType;
    @Embedded
    private Product product;

    @Builder
    public Draw(Long id, Integer totalWinner, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime resultTime,
                DrawType drawType, Product product) {
        this.id = id;
        this.totalWinner = totalWinner;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultTime = resultTime;
        this.drawType = drawType;
        this.product = product;
    }
}
