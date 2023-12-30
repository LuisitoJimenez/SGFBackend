package com.coatl.sac.repository;

import java.sql.Date;
import java.sql.Time;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coatl.sac.entity.GameEntity;

public interface GameRepository extends JpaRepository<GameEntity, Integer> {

    boolean existsByGameDate(Date gameDate);

    boolean existsByGameTimeAndDeletedIsNull(Time gameTime);

    boolean existsByNameAndDeletedIsNull(String name);

    List<GameEntity> findAll();

    @Query(value = """
            SELECT
                game.id,
                game.name,
                game.game_date,
                game.game_time,
                game.field,
                sub.id AS subId,
                gender.id AS genderId,
                referee.id AS refereeId
            FROM games game
                INNER JOIN games_subs gameSub ON game.id = gameSub.game_id AND gameSub.deleted IS NULL
                INNER JOIN subs sub ON gameSub.sub_id = sub.id AND sub.deleted IS NULL
                INNER JOIN games_genders gameGender ON game.id = gameGender.game_id AND gameGender.deleted IS NULL
                INNER JOIN genders gender ON gameGender.gender_id = gender.id AND gender.deleted IS NULL
                INNER JOIN games_referees gameReferee ON game.id = gameReferee.game_id AND gameReferee.deleted IS NULL
                INNER JOIN referees referee ON gameReferee.referee_id = referee.id AND referee.deleted IS NULL
            WHERE game.deleted IS NULL
                    """, nativeQuery = true)
    List<Map<String, Object>> getGameList();

    @Query(value = """
            SELECT
                game.id,
                game.name,
                game.game_date,
                game.game_time,
                game.field,
                sub.id AS subId,
                gender.id AS genderId,
                referee.id AS refereeId
            FROM games game
                INNER JOIN games_subs gameSub ON game.id = gameSub.game_id AND gameSub.deleted IS NULL
                INNER JOIN subs sub ON gameSub.sub_id = sub.id AND sub.deleted IS NULL
                INNER JOIN games_genders gameGender ON game.id = gameGender.game_id AND gameGender.deleted IS NULL
                INNER JOIN genders gender ON gameGender.gender_id = gender.id AND gender.deleted IS NULL
                INNER JOIN games_referees gameReferee ON game.id = gameReferee.game_id AND gameReferee.deleted IS NULL
                INNER JOIN referees referee ON gameReferee.referee_id = referee.id AND referee.deleted IS NULL
            WHERE game.deleted IS NULL AND game.id = :pGameId
                    """, nativeQuery = true)
    Map<String, Object> findGameById(@Param("pGameId") Integer gameId);

    @Query(value = """
            SELECT
                game.id AS gameId,
                game.name AS gameName,
                team.id AS teamId,
                team.name AS teamName,
                team.coach
            FROM games game
                LEFT JOIN games_teams gameTeam ON game.id = gameTeam.game_id
                LEFT JOIN teams team ON gameTeam.team_id = team.id
            WHERE game.id = :pGameId AND gameTeam.deleted IS NULL;
                            """, nativeQuery = true)
    List<Map<String, Object>> getGameTeams(@Param("pGameId") Integer gameId);

}
