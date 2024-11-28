package com.plus.domain.draw.service;

import static com.plus.domain.common.exception.enums.ExceptionCode.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.plus.domain.common.exception.DrawException;
import com.plus.domain.common.exception.ExpectedException;
import com.plus.domain.common.exception.enums.ExceptionCode;
import com.plus.domain.draw.dto.request.DrawSaveRequestDto;
import com.plus.domain.draw.dto.request.DrawSearchRequestDto;
import com.plus.domain.draw.dto.request.DrawUpdateRequestDto;
import com.plus.domain.draw.dto.response.DrawSaveResponseDto;
import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.dto.response.DrawUpdateResponseDto;
import com.plus.domain.draw.dto.response.EntryResultResponseDto;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.entity.UserDraw;
import com.plus.domain.draw.enums.DrawStatus;
import com.plus.domain.draw.enums.EntryResultStatus;
import com.plus.domain.draw.enums.SortBy;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.draw.repository.UserDrawRepository;
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
	private final UserDrawRepository userDrawRepository;
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
			.maxWinnerCount(requestDto.getTotalWinners())
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

	@Transactional
	public DrawUpdateResponseDto updateDraw(Long userId, Long drawId, DrawUpdateRequestDto requestDto,
		MultipartFile image) throws IOException {

		// User 권한 확인
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ExpectedException(ExceptionCode.NOT_AUTHORIZED)); // USER_NOT_FOUND로 변경 예정

		if (user.getUserRole() != UserRole.ADMIN) {
			throw new ExpectedException(ExceptionCode.NOT_AUTHORIZED);
		}

		// 기존 응모 데이터 조회
		Draw draw = drawRepository.findById(drawId)
			.orElseThrow(() -> new ExpectedException(DRAW_NOT_FOUND));

		// 응모 제품명 중복 확인 (본인의 제품 제외)
		if (!draw.getProduct().getProductName().equals(requestDto.getProduct().getProductName()) &&
			drawRepository.existsByProduct_ProductName(requestDto.getProduct().getProductName())) {
			throw new ExpectedException(ExceptionCode.DUPLICATE_DRAW_NAME);
		}

		// 이미지가 새로운 파일로 전달된 경우, S3에 업로드 및 기존 이미지 삭제
		String imageUrl = draw.getProduct().getProductImage(); // 기존 이미지 URL 유지
		if (image != null && !image.isEmpty()) {
			// 기존 이미지 삭제 (선택적으로 구현)
			s3Service.delete(imageUrl);

			// 새로운 이미지 업로드
			imageUrl = s3Service.upload(image, "image");
		}

		// 수정 로직: Draw 엔티티의 필드 업데이트
		draw.setMaxWinnerCount(requestDto.getTotalWinners());
		draw.setStartTime(requestDto.getStartTime());
		draw.setEndTime(requestDto.getEndTime());
		draw.setResultTime(requestDto.getResultTime());
		draw.setDrawType(requestDto.getDrawType());

		// Product 필드 업데이트
		Product product = draw.getProduct();
		product.setProductName(requestDto.getProduct().getProductName());
		product.setProductDescription(requestDto.getProduct().getProductDescription());
		product.setProductImage(imageUrl);

		// 변경 사항 저장
		Draw updateDraw = drawRepository.save(draw);

		return new DrawUpdateResponseDto(updateDraw);

	}

	@Transactional
	public void deleteDraw(Long userId, Long drawId) {
		// Draw 엔티티 확인
		Draw draw = drawRepository.findById(drawId)
			.orElseThrow(() -> new ExpectedException(DRAW_NOT_FOUND));

		// User 권한 확인
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ExpectedException(ExceptionCode.NOT_AUTHORIZED)); // USER_NOT_FOUND로 변경 예정

		if (user.getUserRole() != UserRole.ADMIN) {
			throw new ExpectedException(ExceptionCode.NOT_AUTHORIZED);
		}

		// 이미지 삭제
		String imageUrl = draw.getProduct().getProductImage();
		s3Service.delete(imageUrl);

		// 데이터베이스에서 Draw 삭제
		drawRepository.delete(draw);

	}

	@Transactional(timeout = 10, propagation = Propagation.REQUIRES_NEW)
	public EntryResultResponseDto entry(Long drawId, Long userId) {
		if (userDrawRepository.existsByUserIdAndDrawId(drawId, userId)) {
			throw new DrawException(ALREADY_EXISTS_DRAW_USER);
		}
		Draw draw = drawRepository.findById(drawId)
			.orElseThrow(() -> new DrawException(DRAW_NOT_FOUND));
		if (draw.isDrawClosed()) {
			return new EntryResultResponseDto(EntryResultStatus.FAIL);
		}
		draw.increaseCurrentWinnerCount();
		UserDraw userDraw = UserDraw.createSuccessUserDraw(drawId, userId);
		userDrawRepository.save(userDraw);
		drawRepository.save(draw);
		return new EntryResultResponseDto(EntryResultStatus.SUCCESS);
	}
}
