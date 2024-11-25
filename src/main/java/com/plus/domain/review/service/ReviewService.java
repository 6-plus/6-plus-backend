package com.plus.domain.review.service;

import org.springframework.stereotype.Service;

import com.plus.domain.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
}
