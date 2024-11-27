package com.plus.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plus.domain.user.entity.Favorite;
import com.plus.domain.user.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	List<Favorite> findAllByUserId(Long userId);

	@Query("SELECT u FROM User u JOIN FETCH Favorite f ON u.id = f.userId WHERE f.drawId = :drawId")
	List<User> findNotificationUserByDrawId(@Param("drawId") Long drawId);
}
