package com.plus.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plus.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
