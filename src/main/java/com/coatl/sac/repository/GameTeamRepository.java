package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.GameTeamEntity;

@Repository
public interface GameTeamRepository extends JpaRepository<GameTeamEntity, Integer>{
    
    long countByGameId(Integer gameId);

    boolean existsByGameIdAndTeamId(Integer gameId, Integer teamId);
}
