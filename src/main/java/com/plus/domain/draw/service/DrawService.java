package com.plus.domain.draw.service;

import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.repository.UserDrawRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.plus.domain.draw.repository.DrawRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DrawService {

	private final DrawRepository drawRepository;
	private final UserDrawRepository userDrawRepository;

	@Transactional(readOnly = true)
	public Page<DrawSearchResponseDto> searchMyDraw(Long userId, Pageable pageable) {
		return userDrawRepository.findAllDrawByUserId(userId, pageable);
	}
}
