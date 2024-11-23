package com.plus.domain.draw.service;

import com.plus.domain.draw.dto.request.DrawSaveRequestDto;
import com.plus.domain.draw.dto.response.DrawSaveResponseDto;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.Product;
import org.springframework.stereotype.Service;

import com.plus.domain.draw.repository.DrawRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DrawService {

	private final DrawRepository drawRepository;

    @Transactional
    public DrawSaveResponseDto saveDraw(DrawSaveRequestDto requestDto) {
        //관리자 권한인지 확인
        //이미 등록된 응모인지 확인(응모제품명)

        //save 로직
        Draw draw = Draw.builder()
                .totalWinner(requestDto.getTotalWinners())
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .resultTime(requestDto.getResultTime())
                .drawType(requestDto.getDrawType())
                .product(new Product(
                        requestDto.getProduct().getProductName(),
                        requestDto.getProduct().getProductDescription(),
                        requestDto.getProduct().getProductImage()
                ))
                .build();

        Draw savedDraw = drawRepository.save(draw);

        return DrawSaveResponseDto.builder()
                .id(savedDraw.getId())
                .totalWinner(savedDraw.getTotalWinner())
                .startTime(savedDraw.getStartTime())
                .endTime(savedDraw.getEndTime())
                .resultTime(savedDraw.getResultTime())
                .drawType(savedDraw.getDrawType())
                .createdAt(savedDraw.getCreatedAt())
                .updatedAt(savedDraw.getUpdatedAt())
                .product(savedDraw.getProduct()) // 임베디드 Product 객체도 그대로 반환
                .build();
    }
}
