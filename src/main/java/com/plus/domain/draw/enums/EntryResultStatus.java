package com.plus.domain.draw.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EntryResultStatus {
	FAIL("응모에 실패했습니다."),
	SUCCESS("응모에 성공했습니다.");

	private final String message;

	EntryResultStatus(String message) {
		this.message = message;
	}
}
