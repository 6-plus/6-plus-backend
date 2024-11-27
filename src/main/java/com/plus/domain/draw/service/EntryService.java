package com.plus.domain.draw.service;


import com.plus.domain.draw.dto.response.EntrySaveResponseDto;
import com.plus.domain.draw.entity.UserDraw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.draw.repository.UserDrawRepository;
import com.plus.domain.security.UserDetailsImpl;
import com.plus.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class EntryService {

	private final DrawRepository drawRepository;
	private final UserRepository userRepository;
	private final UserDrawRepository userDrawRepository;

	@Transactional
	public EntrySaveResponseDto saveEntry(Long drawId, UserDetailsImpl userDetails) {
		Long userId = userDetails.getUser().getId();

		UserDraw saveEntry = userDrawRepository.save(UserDraw.builder()
			.drawId(drawId)
			.userId(userId)
			.build());

		return EntrySaveResponseDto.from(saveEntry);
	}
}
