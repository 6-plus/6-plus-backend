package com.plus.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plus.domain.user.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
