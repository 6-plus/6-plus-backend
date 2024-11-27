package com.plus.domain.draw.repository;

import com.plus.domain.draw.entity.UserDrawLock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDrawLcokRepository extends JpaRepository<UserDrawLock, Long>, DrawRepositoryQuery {

}
