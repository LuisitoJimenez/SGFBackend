package com.coatl.sac.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coatl.sac.entity.GameEntity;

public interface GameRepository extends JpaRepository<GameEntity, Integer>{

    boolean existsByGameDate(Date gameDate);

    boolean existsByName(String name);

    List<GameEntity> findAll();

    @Query(value = """
                SELECT * FROM games WHERE deleted IS NULL
            """, nativeQuery = true)
    List<Map<String, Object>> getGameList();

    @Query(value = """
        SELECT
        game.id,
        game.name,
        game.play_date,
        game.referee,
        team.id AS teamId,
        team.name AS teamName,
        team.coach
    FROM games game
        LEFT JOIN games_teams gameTeam ON game.id = gameTeam.game_id
        LEFT JOIN teams team ON gameTeam.team_id = team.id
    WHERE game.id = :pGameId AND gameTeam.deleted IS NULL;
            """, nativeQuery = true
    )
    List<Map<String, Object>> getGameTeams(@Param ("pGameId") Integer gameId);
    

}
