package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.GameTeamEntity;

@Repository
public interface GameTeamRepository extends JpaRepository<GameTeamEntity, Integer>{
    
    long countByGameIdAndDeletedIsNull(Integer gameId);

    boolean existsByGameIdAndTeamIdAndDeletedIsNull(Integer gameId, Integer teamId);

    Optional<GameTeamEntity> findByGameIdAndTeamIdAndDeletedIsNull(Integer gameId, Integer teamId);
}
