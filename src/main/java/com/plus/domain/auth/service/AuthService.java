package com.plus.domain.auth.service;

import static com.plus.domain.common.exception.enums.ExceptionCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus.domain.auth.dto.SignupRequestDto;
import com.plus.domain.common.exception.AuthException;
import com.plus.domain.user.entity.User;
import com.plus.domain.user.enums.UserRole;
import com.plus.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	//회원가입
	public void signup(SignupRequestDto signupRequestDto) {
		if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
			throw new AuthException(DUPLICATE_EMAIL);
		}
		if (userRepository.existsByNickname(signupRequestDto.getNickName())) {
			throw new AuthException(DUPLICATE_EMAIL);
		}

		String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

		// 'USER' 역할을 기본적으로 부여
		UserRole userRole = UserRole.USER;  // 기본적으로 'USER' 역할 부여

		User newUser = new User(
			signupRequestDto.getEmail(),
			encodedPassword,
			signupRequestDto.getNickName(),
			signupRequestDto.getPhoneNumber(),
			userRole
		);

		// 사용자 저장
		userRepository.save(newUser);

	}

}
