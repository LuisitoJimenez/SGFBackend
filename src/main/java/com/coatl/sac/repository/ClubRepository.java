package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;
//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coatl.sac.entity.ClubEntity;

public interface ClubRepository extends JpaRepository<ClubEntity, Integer> {

       @Query(value = """
                     SELECT
                            club.id,
                            JSON_UNQUOTE(JSON_EXTRACT(club.logo, '$[0].name')) AS logo,
                            club.name,
                            club.social_networks AS socialNetworks,
                            club.email,
                            club.phone
                     FROM clubs club
                     WHERE club.deleted IS NULL;
                                        """, nativeQuery = true)
       List<Map<String, Object>> getClubsList();

       /*
        * @Query(value = """
        * SELECT
        * id,
        * name
        * FROM clubs
        * WHERE name = :clubName AND deleted IS NOT NULL
        * """, nativeQuery = true)
        * Boolean findDeletedClubByName(@Param("clubName") String clubName);
        */

       /*
        * @Query(value = """
        * SELECT CASE WHEN EXISTS (
        * SELECT name
        * FROM clubs
        * WHERE name = :clubName
        * )
        * THEN TRUE
        * ELSE FALSE END
        * """, nativeQuery = true)
        * Integer existsDeletedClubByName(@Param("clubName") String clubName);
        */

       @Query(value = """
                     SELECT CASE WHEN EXISTS (
                            SELECT name
                            FROM clubs
                            WHERE name = :clubName AND deleted IS NULL
                        )
                        THEN TRUE
                        ELSE FALSE END
                            """, nativeQuery = true)
       Integer existsDeletedClubByName(@Param("clubName") String clubName);

       boolean existsByName(String name);

       @Query(value = """
                     SELECT
                            club.id,
                            JSON_UNQUOTE(JSON_EXTRACT(club.logo, '$[0].name')) AS logo,
                            club.name,
                            club.social_networks AS socialNetworks,
                            club.email,
                            club.phone
                     FROM clubs club
                     WHERE club.deleted IS NULL AND club.id = :clubId
                                     """, nativeQuery = true)
       Map<String, Object> getClubById(@Param("clubId") Integer clubId);
}
