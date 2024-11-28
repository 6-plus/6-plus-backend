package com.plus.domain.draw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plus.domain.draw.entity.Draw;

import jakarta.persistence.LockModeType;

public interface DrawRepository extends JpaRepository<Draw, Long>, DrawRepositoryQuery {
	boolean existsByProduct_ProductName(String productName);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select d from Draw d where d.id = :drawId")
	Optional<Draw> findByIdForUpdate(@Param("drawId") Long drawId);
}
