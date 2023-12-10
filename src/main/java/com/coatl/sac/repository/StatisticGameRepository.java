package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.StatisticGameEntity;

@Repository
public interface StatisticGameRepository extends JpaRepository<StatisticGameEntity, Integer>{
    
    
}
