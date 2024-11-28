package com.plus.domain.draw.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plus.domain.draw.dto.request.DrawSaveRequestDto;
import com.plus.domain.draw.dto.request.DrawSearchRequestDto;
import com.plus.domain.draw.dto.request.DrawUpdateRequestDto;
import com.plus.domain.draw.dto.response.DrawSaveResponseDto;
import com.plus.domain.draw.dto.response.DrawSearchResponseDto;
import com.plus.domain.draw.dto.response.DrawUpdateResponseDto;
import com.plus.domain.draw.dto.response.EntryResultResponseDto;
import com.plus.domain.draw.service.DrawService;
import com.plus.domain.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/draws")
@RequiredArgsConstructor
public class DrawController {

	private final DrawService drawService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DrawSaveResponseDto> saveDraw(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestPart("requestDto") DrawSaveRequestDto requestDto,
		@RequestPart("image") MultipartFile image) throws IOException {
		DrawSaveResponseDto responseDto = drawService.saveDraw(userDetails.getUser().getId(), requestDto, image);
		return ResponseEntity.ok(responseDto);
	}

	@PatchMapping(value = "/{drawId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DrawUpdateResponseDto> updateDraw(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long drawId,
		@RequestPart("requestDto") DrawUpdateRequestDto requestDto,
		@RequestPart("image") MultipartFile image) throws IOException {

		DrawUpdateResponseDto responseDto = drawService.updateDraw(
			userDetails.getUser().getId(),
			drawId,
			requestDto,
			image);
		return ResponseEntity.ok(responseDto);
	}

	@DeleteMapping("/{drawId}")
	public ResponseEntity<Void> deleteDraw(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long drawId) {
		drawService.deleteDraw(userDetails.getUser().getId(), drawId);
		return ResponseEntity.noContent().build();
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

	@PostMapping("/{drawId}/entries")
	public ResponseEntity<EntryResultResponseDto> entry(
		@PathVariable Long drawId,
		@AuthenticationPrincipal UserDetailsImpl user
	) {
		// POST/api/draws/{drawId}/entries 로 요청이 들어온다.
		Long userId = user.getUser().getId();
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(drawService.entry(drawId, userId));
	}
}
