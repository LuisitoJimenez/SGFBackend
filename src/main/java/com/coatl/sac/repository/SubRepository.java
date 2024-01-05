package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coatl.sac.entity.SubEntity;

public interface SubRepository extends JpaRepository<SubEntity, Integer> {

    @Query(value = """
            SELECT sub.id,
                   sub.name,
                   sub.max_age AS maxAge,
                   sub.min_age AS minAge
            FROM sub
            WHERE sub.deleted_at IS NULL
                    """, nativeQuery = true)
    List<Map<String, Object>> getSubsList();
}
