package com.plus.domain.draw.controller;

import com.plus.domain.draw.dto.response.EntrySaveResponseDto;
import com.plus.domain.draw.service.EntryService;
import com.plus.domain.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/draw")
public class EntryController {

	private final EntryService entryService;

	@PostMapping("/{drawId}/entries")
	public ResponseEntity<EntrySaveResponseDto> saveEntry(
		@PathVariable(name = "drawId") Long drawId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(entryService.saveEntry(drawId, userDetails));
	}
}
