package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.TournamentGenderEntity;

@Repository
public interface TournamentGenderRepository extends JpaRepository<TournamentGenderEntity, Integer> {
    
}
