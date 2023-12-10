package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.UserPlayerEntity;

@Repository
public interface UserPlayerRepository extends JpaRepository<UserPlayerEntity, Integer> {
    
    Optional<UserPlayerEntity> findByUserId (Integer userId);

    Optional<UserPlayerEntity> findByPlayerId (Integer playerId);
}
