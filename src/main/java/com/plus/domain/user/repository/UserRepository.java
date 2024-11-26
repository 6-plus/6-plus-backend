package com.plus.domain.user.repository;

import java.util.Optional;

import com.plus.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

  Optional<User> findByEmail(String email);
}

