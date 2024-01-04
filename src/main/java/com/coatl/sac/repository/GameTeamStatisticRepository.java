package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.GameTeamStatisticEntity;

@Repository
public interface GameTeamStatisticRepository extends JpaRepository<GameTeamStatisticEntity, Integer> {
    
}
