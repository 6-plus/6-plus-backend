package com.plus.domain.draw.dto.response;

import com.plus.domain.draw.enums.EntryResultStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EntryResultResponseDto {
	private final EntryResultStatus status;
}
