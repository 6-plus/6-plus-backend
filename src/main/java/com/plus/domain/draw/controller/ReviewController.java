package com.plus.domain.draw.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plus.domain.draw.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/draws/{drawId}/reviews")
public class ReviewController {

	private final ReviewService reviewService;
}
