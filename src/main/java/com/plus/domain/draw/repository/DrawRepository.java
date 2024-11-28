package com.plus.domain.draw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.plus.domain.draw.entity.Draw;

import jakarta.persistence.LockModeType;

public interface DrawRepository extends JpaRepository<Draw, Long>, DrawRepositoryQuery {
	boolean existsByProduct_ProductName(String productName);

	// 비관적 락을 사용하여 Draw 엔터티를 조회
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT d FROM Draw d WHERE d.id = :drawId")
	Optional<Draw> findById(Long drawId);
}
