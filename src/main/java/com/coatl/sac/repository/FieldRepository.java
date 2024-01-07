package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.FieldEntity;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {

    Optional<FieldEntity> findByNameAndDeletedAtIsNull(String name);

    @Query(value = """
            SELECT field.id,
                   JSON_UNQUOTE(JSON_EXTRACT(field.logo, '$[0].name')) AS logo,
                   field.name,
                   field.address,
                   field.phone,
                   field.email,
                   field.capacity
            FROM field
            WHERE field.deleted_at IS NULL
                        """, nativeQuery = true)
    List<Map<String, Object>> getFieldsList();

    @Query(value = """
            SELECT field.id,
                   JSON_UNQUOTE(JSON_EXTRACT(field.logo, '$[0].name')) AS logo,
                   field.name,
                   field.address,
                   field.phone,
                   field.email,
                   field.capacity
            FROM field
            WHERE field.id=:pfieldId AND field.deleted_at IS NULL
                        """, nativeQuery = true)
    Map<String, Object> getFieldById(@Param("pfieldId") Integer fieldId);

}
