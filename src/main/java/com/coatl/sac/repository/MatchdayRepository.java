package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.MatchdayEntity;

@Repository
public interface MatchdayRepository extends JpaRepository<MatchdayEntity, Integer> {
    
}
