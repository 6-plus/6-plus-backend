package com.plus.domain.draw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plus.domain.draw.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
