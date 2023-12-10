package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.UserTeamEntity;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeamEntity, Integer>{
    
    Optional<UserTeamEntity> findByUserIdAndTeamId(Integer userId, Integer teamId);

    Optional<UserTeamEntity> findByUserId(Integer userId);

   
}
