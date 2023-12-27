package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coatl.sac.entity.SubsEntity;

public interface SubsRepository extends JpaRepository<SubsEntity, Integer> {
    
    @Query(value = """
                  SELECT
                         subs.id,
                         subs.name,
                         subs.max_age AS maxAge,
                         subs.min_age AS minAge
                  FROM subs
                  WHERE subs.deleted IS NULL;
                                     """, nativeQuery = true)
    List<Map<String, Object>> getSubsList();
}
