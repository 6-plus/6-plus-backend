package com.plus.domain.draw.repository;

import com.plus.domain.draw.entity.UserDraw;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDrawRepository extends JpaRepository<UserDraw, Long>, UserDrawRepositoryQuery {
}
