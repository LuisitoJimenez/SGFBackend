package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.ClubTeamEntity;

@Repository
public interface ClubTeamRepository extends JpaRepository<ClubTeamEntity, Integer> {
    
    //boolean existsByClubIdAndTeamId(Integer clubId, Integer teamId);

    Optional<ClubTeamEntity> findByClubIdAndTeamId(Integer clubId, Integer teamId);

    Optional<ClubTeamEntity> findByTeamIdAndDeletedAtIsNull(Integer teamId);
}
