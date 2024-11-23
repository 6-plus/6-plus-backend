package com.plus.domain.draw.controller;

import com.plus.domain.draw.dto.request.DrawSaveRequestDto;
import com.plus.domain.draw.dto.response.DrawSaveResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plus.domain.draw.service.DrawService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/draws")
@RequiredArgsConstructor
public class DrawController {

	private final DrawService drawService;

	@PostMapping
	public ResponseEntity<DrawSaveResponseDto> saveDraw(@RequestBody DrawSaveRequestDto requestDto){
		DrawSaveResponseDto responseDto = drawService.saveDraw(requestDto);
		return ResponseEntity.ok(responseDto);
	}
}
