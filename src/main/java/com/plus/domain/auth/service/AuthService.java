package com.plus.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus.domain.auth.dto.SignupRequestDto;
import com.plus.domain.auth.exception.InvalidRequestException;
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
			throw new InvalidRequestException("이미 존재하는 이메일입니다.");
		}
		if (userRepository.existsByNickname(signupRequestDto.getNickName())) {
			throw new InvalidRequestException("이미 존재하는 닉네임입니다.");
		}

		String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

		// 'USER' 역할을 명시적으로 부여
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
