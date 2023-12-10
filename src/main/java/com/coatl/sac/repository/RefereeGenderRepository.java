package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.RefereeGenderEntity;

@Repository
public interface RefereeGenderRepository extends JpaRepository<RefereeGenderEntity, Integer> {

    Optional<RefereeGenderEntity> findByRefereeId(Integer refereeId);

}
