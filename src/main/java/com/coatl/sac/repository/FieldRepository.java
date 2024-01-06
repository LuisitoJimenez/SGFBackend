package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.FieldEntity;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {

    Optional<FieldEntity> findByNameAndDeletedAtIsNull(String name);

    @Query(value = """
            SELECT field.id,
                   field.logo,
                   field.name,
                   field.address,
                   field.phone,
                   field.email,
                   field.capacity
            FROM field
            WHERE field.deleted_at IS NULL
                        """, nativeQuery = true)
    List<Map<String, Object>> getFieldsList();

}
