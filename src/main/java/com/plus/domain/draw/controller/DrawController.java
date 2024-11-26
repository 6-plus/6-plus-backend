package com.plus.domain.draw.controller;

import com.plus.domain.draw.dto.request.DrawSaveRequestDto;
import com.plus.domain.draw.dto.request.DrawSearchRequestDto;
import com.plus.domain.draw.dto.response.DrawSaveResponseDto;
import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.service.DrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/draws")
@RequiredArgsConstructor
public class DrawController {

	private final DrawService drawService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DrawSaveResponseDto> saveDraw(@RequestPart("requestDto") DrawSaveRequestDto requestDto,
														@RequestPart("image") MultipartFile image) throws IOException {
		DrawSaveResponseDto responseDto = drawService.saveDraw(requestDto, image);
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/my")
	public ResponseEntity<List<DrawSearchResponseDto>> searchMyDraw(
		@RequestParam Long userId,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(drawService.searchMyDraw(userId, page, size));
	}

	@GetMapping
	public ResponseEntity<List<DrawSearchResponseDto>> searchDraws(
		@RequestBody DrawSearchRequestDto requestDto,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(drawService.searchDraws(requestDto, page, size));
	}
}
