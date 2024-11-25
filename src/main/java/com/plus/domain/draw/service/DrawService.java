package com.plus.domain.draw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.draw.repository.UserDrawRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrawService {

	private final DrawRepository drawRepository;

	@Transactional(readOnly = true)
	public List<DrawSearchResponseDto> searchMyDraw(Long userId, int page, int size) {
		return drawRepository.findAllDrawByUserId(userId, page, size);
	}
}
