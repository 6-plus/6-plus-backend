package com.plus.domain.draw.service;

import com.plus.domain.draw.dto.request.DrawSaveRequestDto;
import com.plus.domain.draw.dto.request.DrawSearchRequestDto;
import com.plus.domain.draw.dto.response.DrawSaveResponseDto;
import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.enums.DrawStatus;
import com.plus.domain.draw.enums.SortBy;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrawService {

	private final DrawRepository drawRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    @Transactional
    public DrawSaveResponseDto saveDraw(DrawSaveRequestDto requestDto, MultipartFile image) throws IOException {
        //User 권한 확인

        //응모 중복 확인 (응모제품명)

        // 이미지 업로드
        String imageUrl = s3Service.upload(image, "image");

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
                        imageUrl
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

	@Transactional(readOnly = true)
	public List<DrawSearchResponseDto> searchMyDraw(Long userId, int page, int size) {
		return drawRepository.findAllDrawByUserId(userId, page, size);
	}

	@Transactional(readOnly = true)
	public List<DrawSearchResponseDto> searchDraws(DrawSearchRequestDto requestDto, int page, int size) {
		String productName = requestDto.getProductName();
		DrawStatus status = requestDto.getStatus();
		LocalDateTime now = LocalDateTime.now();
		SortBy sortBy = requestDto.getSortBy();
		return drawRepository.findAllDraws(productName, page, size, status, now, sortBy);
	}
}
