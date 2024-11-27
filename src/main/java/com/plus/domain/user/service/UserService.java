package com.plus.domain.user.service;

import static com.plus.domain.common.exception.enums.ExceptionCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.plus.domain.common.exception.UserException;
import com.plus.domain.security.UserDetailsImpl;
import com.plus.domain.user.dto.request.UserDeleteRequestDto;
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

		User user = userRepository.findById(userDetails.getUser().getId())
			.orElseThrow(() -> new UserException(USER_NOT_FOUND));
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
		userRepository.save(user);
	}

	@Transactional
	public void deleteUser(UserDeleteRequestDto requestDto, UserDetailsImpl userDetails) {
		User user = userDetails.getUser();
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new UserException(INVALID_PASSWORD);
		}
		userRepository.delete(user);
	}

}
