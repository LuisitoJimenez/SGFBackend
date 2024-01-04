package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.FieldGameEntity;

@Repository
public interface FieldGameRepository extends JpaRepository<FieldGameEntity, Integer> {
    
}
