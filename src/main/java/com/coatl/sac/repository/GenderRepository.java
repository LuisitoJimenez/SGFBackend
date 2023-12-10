package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.GenderEntity;

@Repository
public interface GenderRepository extends JpaRepository<GenderEntity, Integer> {
    Optional<GenderEntity> findByName(String gender);
}
