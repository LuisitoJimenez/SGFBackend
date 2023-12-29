package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.GameGenderEntity;

@Repository
public interface GameGenderRepository extends JpaRepository<GameGenderEntity, Integer>{
    
}
