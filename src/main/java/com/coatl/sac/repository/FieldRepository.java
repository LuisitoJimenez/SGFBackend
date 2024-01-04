package com.coatl.sac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.FieldEntity;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {
    
    Optional<FieldEntity> findByNameAndDeletedAtIsNull(String name);

}
