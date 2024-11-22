package com.plus.domain.draw.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plus.domain.draw.service.DrawService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/draws")
@RequiredArgsConstructor
public class DrawController {

	private final DrawService drawService;
}
