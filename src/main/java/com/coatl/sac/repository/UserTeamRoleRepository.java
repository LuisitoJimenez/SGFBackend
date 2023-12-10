package com.coatl.sac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.UserTeamRoleEntity;

@Repository
public interface UserTeamRoleRepository extends JpaRepository<UserTeamRoleEntity, Integer> {
    
    //Optional<UserTeamRoleEntity> findByUserIdAndRoleId(Integer userId, Integer roleId);

}
