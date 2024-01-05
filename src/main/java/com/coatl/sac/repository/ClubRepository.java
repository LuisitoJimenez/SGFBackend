package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coatl.sac.entity.ClubEntity;

public interface ClubRepository extends JpaRepository<ClubEntity, Integer> {

       @Query(value = """
                     SELECT club.id,
                            JSON_UNQUOTE(JSON_EXTRACT(club.logo, '$[0].name')) AS logo,
                            club.name,
                            club.social_networks                               AS socialNetworks,
                            club.email,
                            club.phone
                     FROM club
                     WHERE club.deleted_at IS NULL
                     """, nativeQuery = true)
       List<Map<String, Object>> getClubsList();

       @Query(value = """
                     SELECT CASE WHEN EXISTS (
                            SELECT name
                            FROM club
                            WHERE name = :clubName AND deleted_at IS NULL
                        )
                        THEN TRUE
                        ELSE FALSE END
                     """, nativeQuery = true)
       Integer existsDeletedClubByName(@Param("clubName") String clubName);

       boolean existsByName(String name);

       @Query(value = """
                     SELECT club.id,
                            JSON_UNQUOTE(JSON_EXTRACT(club.logo, '$[0].name')) AS logo,
                            club.name,
                            club.social_networks                               AS socialNetworks,
                            club.email,
                            club.phone
                     FROM club
                     WHERE club.deleted_at IS NULL
                       AND club.id = :clubId
                     """, nativeQuery = true)
       Map<String, Object> getClubById(@Param("clubId") Integer clubId);
}
