package com.plus.domain.user.service;

import static com.plus.domain.common.exception.enums.ExceptionCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.plus.domain.common.exception.UserException;
import com.plus.domain.security.UserDetailsImpl;
import com.plus.domain.user.dto.request.UserUpdateRequestDto;
import com.plus.domain.user.entity.User;
import com.plus.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void updateUserInfo(UserUpdateRequestDto requestDto, UserDetailsImpl userDetails) {

		// 현재 사용자 가져오기
		User user = userRepository.findById(userDetails.getUser().getId())
			.orElseThrow(() -> new UserException(USER_DOES_NOT_EXIST));

		if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
			throw new UserException(PASSWORD_MISMATCH);
		}

		if (requestDto.getNickname() != null && !requestDto.getNickname().isBlank()) {
			if (userRepository.existsByNickname(requestDto.getNickname())) {
				throw new UserException(NICKNAME_ALREADY_IN_USE);
			}
			user.setNickname(requestDto.getNickname());
		}

		if (requestDto.getPhoneNumber() != null && !requestDto.getPhoneNumber().isBlank()) {
			user.setPhoneNumber(requestDto.getPhoneNumber());
		}

		if (requestDto.getNewPassword() != null && !requestDto.getNewPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
		}

		// 사용자 정보 저장
		userRepository.save(user);
	}

}
