package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.TournamentSubEntity;

@Repository
public interface TournamentSubRepository extends JpaRepository<TournamentSubEntity, Integer>{
    
}
