package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.TournamentGameEntity;

@Repository
public interface TournamentGameRepository extends JpaRepository<TournamentGameEntity, Integer>{
    
}
