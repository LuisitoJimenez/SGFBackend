package com.coatl.sac.repository;

import java.sql.Date;
import java.sql.Time;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coatl.sac.entity.GameEntity;

public interface GameRepository extends JpaRepository<GameEntity, Integer> {

    boolean existsByGameDate(Date gameDate);

    boolean existsByGameTimeAndDeletedAtIsNull(Time gameTime);

    boolean existsByNameAndDeletedAtIsNull(String name);

    List<GameEntity> findAll();

    @Query(value = """
            SELECT gm.id,
                   gm.name,
                   gm.game_date,
                   gm.game_time,
                   s.name    AS sub,
                   g.name AS gender,
                   u.name   AS referee
            FROM game gm
                    INNER JOIN game_sub gs ON gm.id = gs.game_id AND gs.deleted_at IS NULL
                    INNER JOIN sub s ON gs.sub_id = s.id AND s.deleted_at IS NULL
                    INNER JOIN game_gender gg ON gm.id = gg.game_id AND gg.deleted_at IS NULL
                    INNER JOIN gender g ON gg.gender_id = g.id AND g.deleted_at IS NULL
                    INNER JOIN game_referee gr ON g.id = gr.game_id AND gr.deleted_at IS NULL
                    INNER JOIN referee r ON gr.referee_id = r.id AND r.deleted_at IS NULL
                    INNER JOIN user_referee ur ON r.id = ur.referee_id AND ur.deleted_at IS NULL
                    INNER JOIN user u ON ur.user_id = u.id AND u.deleted_at IS NULL
            WHERE gm.deleted_at IS NULL
                    """, nativeQuery = true)
    List<Map<String, Object>> getGameList();

    @Query(value = """
            SELECT gm.id,
                   gm.name,
                   gm.game_date AS gameDate,
                   gm.game_time AS gameTime,
                   sub.id       AS subId,
                   g.id         AS genderId,
                   r.id         AS refereeId
            FROM game gm
                    INNER JOIN game_sub gs ON gm.id = gs.game_id AND gs.deleted_at IS NULL
                    INNER JOIN sub sub ON gs.sub_id = sub.id AND sub.deleted_at IS NULL
                    INNER JOIN game_gender gg ON gm.id = gg.game_id AND gg.deleted_at IS NULL
                    INNER JOIN gender g ON gg.gender_id = g.id AND g.deleted_at IS NULL
                    INNER JOIN game_referee gr ON gm.id = gr.game_id AND gr.deleted_at IS NULL
                    INNER JOIN referee r ON gr.referee_id = r.id AND r.deleted_at IS NULL
            WHERE gm.deleted_at IS NULL
              AND gm.id = :pGameId;
                               """, nativeQuery = true)
    Map<String, Object> findGameById(@Param("pGameId") Integer gameId);

    @Query(value = """
            SELECT g.id   AS gameId,
                   g.name AS gameName,
                   t.id   AS teamId,
                   t.name AS teamName,
                   t.coach
            FROM game g
                     LEFT JOIN game_team gt ON g.id = gt.game_id
                     LEFT JOIN team t ON gt.team_id = t.id
            WHERE g.id = :pGameId
              AND gt.deleted_at IS NULL
                    """, nativeQuery = true)
    List<Map<String, Object>> getGameTeams(@Param("pGameId") Integer gameId);

}
