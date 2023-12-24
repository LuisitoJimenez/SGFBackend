package com.coatl.sac.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.TeamSubEntity;

@Repository
public interface TeamSubRepository extends JpaRepository<TeamSubEntity, Integer>{

    Optional<TeamSubEntity> findByTeamIdAndDeletedIsNull(Integer teamId);
}
