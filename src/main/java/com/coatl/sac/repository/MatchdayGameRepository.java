package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.MatchdayGameEntity;

@Repository
public interface MatchdayGameRepository extends JpaRepository<MatchdayGameEntity, Integer> {
    
}
