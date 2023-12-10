package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.UserRefereeEntity;

@Repository
public interface UserRefereeRepository extends JpaRepository<UserRefereeEntity, Integer> {
    
        Optional<UserRefereeEntity> findByRefereeId (Integer refereeId);

        Optional<UserRefereeEntity> findByUserId (Integer userId);


}
