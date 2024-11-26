package com.plus.domain.draw.dto.request;

import com.plus.domain.draw.enums.DrawStatus;
import com.plus.domain.draw.enums.SortBy;

import lombok.Getter;

@Getter
public class DrawSearchRequestDto {

	private String productName;
	private DrawStatus status = DrawStatus.BEFORE_DRAW;
	private SortBy sortBy = SortBy.END_TIME_DEC;

}
