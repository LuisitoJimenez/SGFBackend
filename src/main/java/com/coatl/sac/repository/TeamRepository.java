package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//import com.coatl.sac.entity.ClubTeamEntity;
import com.coatl.sac.entity.TeamEntity;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {

    List<TeamEntity> findAll();

    @Query(value = """
            SELECT t.id    AS id,
                   t.name  AS name,
                   t.coach AS coach,
                   g.name  AS gender,
                   s.name  AS sub,
                   c.name  AS club
            FROM team t
                    INNER JOIN team_sub ts ON t.id = ts.team_id AND ts.deleted_at IS NULL
                    INNER JOIN sub s ON ts.sub_id = s.id AND s.deleted_at IS NULL
                    INNER JOIN team_gender tg ON t.id = tg.team_id AND tg.deleted_at IS NULL
                    INNER JOIN gender g ON tg.gender_id = g.id
                    INNER JOIN club_team ct ON t.id = ct.team_id AND ct.deleted_at IS NULL
                    INNER JOIN club c ON ct.club_id = c.id
            WHERE t.deleted_at IS NULL
                    """, nativeQuery = true)
    List<Map<String, Object>> getTeamList();

    @Query(value = """
            SELECT t.id   AS id,
                   t.name,
                   t.coach,
                   g.id AS gender,
                   s.id    AS sub,
                   c.name AS club
            FROM team t
                    INNER JOIN team_sub ts ON t.id = ts.team_id AND ts.deleted_at IS NULL
                    INNER JOIN sub s ON ts.sub_id = s.id AND s.deleted_at IS NULL
                    INNER JOIN team_gender tg ON t.id = tg.team_id AND tg.deleted_at IS NULL
                    INNER JOIN gender g ON tg.gender_id = g.id
                    INNER JOIN club_team ct ON t.id = ct.team_id AND ct.deleted_at IS NULL
                    INNER JOIN club c ON ct.club_id = c.id
            WHERE t.deleted_at IS NULL
              AND t.id = :pTeamId
                    """, nativeQuery = true)
    Map<String, Object> getTeamById(Integer pTeamId);

    Optional<TeamEntity> findByIdAndDeletedAtIsNull(Integer teamId);

}
