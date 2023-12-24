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
                team.id AS teamId,
                team.name AS name,
                team.coach AS coach,
                gender.id AS genderId,
                sub.id AS subId,
                club.id AS clubId
            FROM teams team
                INNER JOIN teams_subs teamSub ON team.id = teamSub.team_id AND teamSub.deleted IS NULL
                INNER JOIN subs sub ON teamSub.sub_id = sub.id AND sub.deleted IS NULL
                INNER JOIN teams_genders teamGender ON team.id = teamGender.team_id AND teamGender.deleted IS NULL
                INNER JOIN genders gender ON teamGender.gender_id = gender.id
                INNER JOIN clubs_teams clubTeam ON team.id = clubTeam.team_id AND clubTeam.deleted IS NULL
                INNER JOIN clubs club ON clubTeam.club_id = club.id
            WHERE team.deleted IS NULL
                                """, nativeQuery = true)
    List<Map<String, Object>> getTeamList();

    @Query(value = """
            SELECT
                team.id,
                team.name,
                team.coach,
                gender.id AS genderId,
                sub.id AS subId,
                club.id AS clubId
            FROM teams team
                INNER JOIN teams_subs teamSub ON team.id = teamSub.team_id AND teamSub.deleted IS NULL
                INNER JOIN subs sub ON teamSub.sub_id = sub.id AND sub.deleted IS NULL
                INNER JOIN teams_genders teamGender ON team.id = teamGender.team_id AND teamGender.deleted IS NULL
                INNER JOIN genders gender ON teamGender.gender_id = gender.id
                INNER JOIN clubs_teams clubTeam ON team.id = clubTeam.team_id AND clubTeam.deleted IS NULL
                INNER JOIN clubs club ON clubTeam.club_id = club.id
            WHERE team.deleted IS NULL AND team.id = :pTeamId
                        """, nativeQuery = true)
    Map<String, Object> getTeamById(Integer pTeamId);

}
