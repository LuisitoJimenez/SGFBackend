package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.GameSubEntity;

@Repository
public interface GameSubRepository extends JpaRepository<GameSubEntity, Integer> {
    
}
