package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.TeamStatisticEntity;

@Repository
public interface TeamStatisticRepository extends JpaRepository<TeamStatisticEntity, Integer> {
    
}
