package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.PlayerGenderEntity;

@Repository
public interface PlayerGenderRepository extends JpaRepository<PlayerGenderEntity, Integer> {

    Optional<PlayerGenderEntity> findByPlayerIdAndGenderId(Integer userId, Integer genderId);

    Optional<PlayerGenderEntity> findByPlayerId(Integer userId);

    
}
