package com.plus.domain.draw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plus.domain.draw.entity.UserDraw;

public interface UserDrawRepository extends JpaRepository<UserDraw, Long> {
	Boolean existsByUserIdAndDrawId(Long userId, Long drawId);

	long countByDrawIdAndWin(Long drawId, boolean b);
}
