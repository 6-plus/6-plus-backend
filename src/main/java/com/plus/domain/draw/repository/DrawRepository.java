package com.plus.domain.draw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plus.domain.draw.entity.Draw;

public interface DrawRepository extends JpaRepository<Draw, Long> {
}
