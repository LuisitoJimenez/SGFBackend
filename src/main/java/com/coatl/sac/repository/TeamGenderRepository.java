package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.TeamGenderEntity;

@Repository
public interface TeamGenderRepository extends JpaRepository<TeamGenderEntity, Integer> {
    
    Optional<TeamGenderEntity> findByTeamIdAndDeletedIsNull(Integer teamId);
}
