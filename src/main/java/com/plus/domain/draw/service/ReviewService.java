package com.plus.domain.draw.service;

import org.springframework.stereotype.Service;

import com.plus.domain.draw.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
}
