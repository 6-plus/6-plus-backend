package com.plus.domain.draw.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.plus.domain.common.exception.ExpectedException;
import com.plus.domain.common.exception.enums.ExceptionCode;
import com.plus.domain.draw.dto.request.DrawSaveRequestDto;
import com.plus.domain.draw.dto.request.DrawSearchRequestDto;
import com.plus.domain.draw.dto.request.DrawUpdateRequestDto;
import com.plus.domain.draw.dto.response.DrawSaveResponseDto;
import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.dto.response.DrawUpdateResponseDto;
import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.entity.Product;
import com.plus.domain.draw.entity.UserDraw;
import com.plus.domain.draw.enums.DrawStatus;
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
	private final NotificationService notificationService;
	private final RedisTemplate redisTemplate;
	private final UserDrawRepository userDrawRepository;

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
			.orElseThrow(() -> new ExpectedException(ExceptionCode.DRAW_NOT_FOUND));

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
		draw.setTotalWinner(requestDto.getTotalWinners());
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
			.orElseThrow(() -> new ExpectedException(ExceptionCode.DRAW_NOT_FOUND));

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

	@Transactional
	public String applyDraw(Long userId, Long drawId) {
		String lockKey = "draw:" + drawId;
		String lockValue = String.valueOf(userId);

		try {
			// 락을 얻기 위한 시도: SETNX (set if not exists)
			while (!redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 1000, TimeUnit.SECONDS)) {
				Thread.sleep(100);
			}
			System.out.println("락 획득: 응모 신청 처리 중...");
			// 4. 이미 응모했는지 확인
			if (userDrawRepository.existsByUserIdAndDrawId(userId, drawId)) {
				throw new ExpectedException(ExceptionCode.ALREADY_APPLIED);
			}
			// 1. Draw 테이블에서 totalWinner 값 가져오기
			Draw draw = drawRepository.findById(drawId)
				.orElseThrow(() -> new ExpectedException(ExceptionCode.DRAW_NOT_FOUND));
			int totalWinner = draw.getTotalWinner();
			// 2. DB에서 당첨된 인원 수 확인
			long winnersCount = userDrawRepository.countByDrawIdAndWin(drawId, true);

			// 3. 당첨된 인원 수가 최대 인원 수를 초과한 경우 예외 처리
			if (winnersCount >= totalWinner) {
				throw new ExpectedException(ExceptionCode.MAXIMUM_PARTICIPANTS_REACHED);
			}
			// 6. DB에 응모 정보 저장
			UserDraw userDraw = new UserDraw();
			userDraw.setUserId(userId);
			userDraw.setDrawId(drawId);
			userDraw.setWin(true); // 기본값으로 비당첨 설정
			userDrawRepository.save(userDraw);
			System.out.println("응모 성공! DB에 저장 완료");
			return "응모 성공!";
		} catch (Exception e) {
			throw new ExpectedException(ExceptionCode.INTERNAL_SERVER_ERROR);
		} finally {
			// 락 해제
			// if (lockValue.equals(redisTemplate.opsForValue().get(lockKey))) {
			redisTemplate.delete(lockKey);  // 락 키 삭제
			// }
		}
	}

}
