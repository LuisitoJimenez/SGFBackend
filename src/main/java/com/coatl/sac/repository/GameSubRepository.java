package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.GameSubEntity;

@Repository
public interface GameSubRepository extends JpaRepository<GameSubEntity, Integer> {
    
    Optional<GameSubEntity> findByGameId(Integer gameId);

    Optional<GameSubEntity> findByGameIdAndDeletedIsNull(Integer gameId);
    
}
