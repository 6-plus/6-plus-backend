package com.plus.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plus.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
