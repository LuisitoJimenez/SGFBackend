package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.TeamSubEntity;

@Repository
public interface TeamSubRepository extends JpaRepository<TeamSubEntity, Integer>{

    Optional<TeamSubEntity> findByTeamIdAndDeletedAtIsNull(Integer teamId);

    //Optional<TeamSubEntity> findByTeamIdAndDeletedIsNull(Integer teamId);
}
