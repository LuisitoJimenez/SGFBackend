package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.GameGenderEntity;
import com.coatl.sac.entity.TeamGenderEntity;

@Repository
public interface GameGenderRepository extends JpaRepository<GameGenderEntity, Integer>{
    
    //Optional<GameGenderEntity> findByGameId(Integer gameId);

    Optional<GameGenderEntity> findByGameIdAndDeletedIsNull(Integer gameId);

    Optional<GameGenderEntity> findByGameIdAndGenderId(Integer gameId, Integer genderId);

    Optional<GameGenderEntity> findByGameId(Integer gameId);
}
