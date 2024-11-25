package com.plus.domain.draw.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.enums.DrawStatus;
import com.plus.domain.draw.enums.SortBy;

public interface DrawRepositoryQuery {
	List<DrawSearchResponseDto> findAllDrawByUserId(Long userId, int page, int size);

	List<DrawSearchResponseDto> findAllDraws(String productName, int page, int size, DrawStatus status, LocalDateTime now, SortBy sortBy);
}
