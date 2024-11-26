package com.plus.domain.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus.domain.auth.dto.SigninRequestDto;
import com.plus.domain.user.enums.UserRole;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/api/auth/signin");

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		log.info("로그인 시도");
		try {
			SigninRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
				SigninRequestDto.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					requestDto.getEmail(),
					requestDto.getPassword(),
					null
				)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		log.info("로그인 성공 및 JWT 생성");
		Long userId = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getId();
		String email = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getEmail();
		UserRole role = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getUserRole();

		// JWT 토큰 생성
		String token = jwtUtil.createToken(userId, email, role);

		String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

		Cookie cookie = new Cookie("Authorization", encodedToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24);

		response.addCookie(cookie);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		log.info("로그인 실패");
		response.setStatus(401);
	}
}
