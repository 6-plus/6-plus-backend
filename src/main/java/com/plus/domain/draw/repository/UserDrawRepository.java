package com.plus.domain.draw.repository;

import java.util.Optional;

import com.plus.domain.draw.entity.UserDraw;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDrawRepository extends JpaRepository<UserDraw, Long> {
	Boolean existsByUserIdAndDrawId(Long userId, Long drawId);

	Optional<UserDraw> findByUserIdAndDrawId(Long userId, Long drawId);

	int countByDrawId(Long drawId);
}
