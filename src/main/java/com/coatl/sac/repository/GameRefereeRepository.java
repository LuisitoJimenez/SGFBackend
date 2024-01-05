package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.GameRefereeEntity;

@Repository
public interface GameRefereeRepository extends JpaRepository<GameRefereeEntity, Integer> {
    
    Optional<GameRefereeEntity> findByGameId(Integer gameId);

    Optional<GameRefereeEntity> findByGameIdAndDeletedAtIsNull(Integer gameId);
}
