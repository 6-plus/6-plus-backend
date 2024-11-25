package com.plus.domain.draw.repository;

import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserDrawRepositoryQuery {
    Page<DrawSearchResponseDto> findAllDrawByUserId(Long userId, Pageable pageable);
}
