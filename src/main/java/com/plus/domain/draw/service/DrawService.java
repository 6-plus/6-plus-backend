package com.plus.domain.draw.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.plus.domain.common.exception.ExpectedException;
import com.plus.domain.common.exception.enums.ExceptionCode;
import com.plus.domain.draw.dto.request.DrawSaveRequestDto;
import com.plus.domain.draw.dto.request.DrawSearchRequestDto;
import com.plus.domain.draw.dto.response.DrawSaveResponseDto;
import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.enums.DrawStatus;
import com.plus.domain.draw.enums.SortBy;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.notification.service.NotificationService;
import com.plus.domain.user.entity.User;
import com.plus.domain.user.enums.UserRole;
import com.plus.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrawService {

	private final DrawRepository drawRepository;
	private final S3Service s3Service;
	private final UserRepository userRepository;
	private final NotificationService notificationService;

	@Transactional
	public DrawSaveResponseDto saveDraw(Long userid, DrawSaveRequestDto requestDto, MultipartFile image) throws
		IOException {

		//User 권한 확인
		User user = userRepository.findById(userid)
			.orElseThrow(() -> new ExpectedException(ExceptionCode.NOT_AUTHORIZED));//USER_NOT_FOUND로 변환예정

		if (user.getUserRole() != UserRole.ADMIN) {
			throw new ExpectedException(ExceptionCode.NOT_AUTHORIZED);
		}
		//응모 중복 확인 (응모제품명)
		if (drawRepository.existsByProduct_ProductName(requestDto.getProduct().getProductName())) {
			throw new ExpectedException(ExceptionCode.DUPLICATE_DRAW_NAME);
		}

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

		draw = drawRepository.save(draw);
		notificationService.addTask(draw);

		return new DrawSaveResponseDto(draw);
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
