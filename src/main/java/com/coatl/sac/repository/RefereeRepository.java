package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.RefereeEntity;

@Repository
public interface RefereeRepository extends JpaRepository<RefereeEntity, Integer> {

       @Query(value = """
                     SELECT r.id,
                            u.name,
                            u.id                                                AS userId,
                            g.id                                              AS genderId,
                            JSON_UNQUOTE(JSON_EXTRACT(r.photo, '$[0].name')) AS photo,
                            r.identification,
                            r.birthplace,
                            r.birthday,
                            r.weight,
                            r.height
                     FROM user u
                            INNER JOIN user_referee ur ON u.id = ur.user_id AND ur.deleted_at IS NULL
                            INNER JOIN referee r ON ur.referee_id = r.id
                            INNER JOIN referee_gender rg ON r.id = rg.referee_id AND rg.deleted_at IS NULL
                            INNER JOIN gender g ON rg.gender_id = g.id
                     WHERE u.deleted_at IS NULL
                       AND r.deleted_at IS NULL
                     """, nativeQuery = true)
       List<Map<String, Object>> getRefereesList();

       @Query(value = """
                     SELECT r.id,
                            u.name,
                            g.id AS genderId,
                            r.photo,
                            r.identification,
                            r.birthplace,
                            r.birthday,
                            r.weight,
                            r.height
                     FROM user u
                              INNER JOIN user_referee ur ON u.id = ur.user_id AND ur.deleted_at IS NULL
                              INNER JOIN referee r ON ur.referee_id = r.id
                              INNER JOIN referee_gender rg ON r.id = rg.referee_id AND rg.deleted_at IS NULL
                              INNER JOIN gender g ON rg.gender_id = g.id
                     WHERE u.deleted_at IS NULL
                       AND r.id = :pRefereeId
                     """, nativeQuery = true)
       Map<String, Object> getRefereeById(@Param("pRefereeId") Integer playerId);
}
