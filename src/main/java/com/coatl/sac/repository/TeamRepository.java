package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.TeamEntity;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {

    List<TeamEntity> findAll();

    @Query(value = """
            SELECT
                team.id,
                team.name,
                team.coach,
                gender.name AS gender,
                age.name AS age
            FROM teams team
            LEFT JOIN teams_genders teamGender ON team.id = teamGender.team_id AND teamGender.deleted IS NULL
            LEFT JOIN genders gender ON teamGender.gender_id = gender.id
            LEFT JOIN teams_ages teamAge ON team.id = teamAge.team_id AND teamAge.deleted IS NULL
            LEFT JOIN ages age ON teamAge.age_id = age.id
            WHERE team.deleted IS NULL
                        """, nativeQuery = true)
    List<Map<String, Object>> getTeamList();

}
