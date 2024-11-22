package com.plus.domain.draw.service;

import org.springframework.stereotype.Service;

import com.plus.domain.draw.repository.DrawRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrawService {

	private final DrawRepository drawRepository;
}
