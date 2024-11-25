package com.plus.domain.draw.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus.domain.draw.dto.request.DrawSearchRequestDto;
import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.enums.DrawStatus;
import com.plus.domain.draw.enums.SortBy;
import com.plus.domain.draw.repository.DrawRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrawService {

	private final DrawRepository drawRepository;

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
