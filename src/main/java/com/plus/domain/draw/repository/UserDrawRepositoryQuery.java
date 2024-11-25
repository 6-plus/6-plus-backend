package com.plus.domain.draw.repository;

import java.util.List;

import com.plus.domain.draw.dto.response.DrawSearchResponseDto;

public interface UserDrawRepositoryQuery {
	List<DrawSearchResponseDto> findAllDrawByUserId(Long userId, int page, int size);
}
