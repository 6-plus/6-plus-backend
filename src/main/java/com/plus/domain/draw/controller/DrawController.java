package com.plus.domain.draw.controller;

import com.plus.domain.draw.dto.request.DrawSaveRequestDto;
import com.plus.domain.draw.dto.response.DrawSaveResponseDto;
import com.plus.domain.draw.entity.Product;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.plus.domain.draw.service.DrawService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/draws")
@RequiredArgsConstructor
public class DrawController {

	private final DrawService drawService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DrawSaveResponseDto> saveDraw(@RequestParam Long userId,
														@RequestPart("requestDto") DrawSaveRequestDto requestDto,
														@RequestPart("image") MultipartFile image) throws IOException {
		DrawSaveResponseDto responseDto = drawService.saveDraw(userId, requestDto, image);
		return ResponseEntity.ok(responseDto);
	}
}
