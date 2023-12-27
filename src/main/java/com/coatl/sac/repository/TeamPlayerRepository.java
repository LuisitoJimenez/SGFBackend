package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.TeamPlayerEntity;

@Repository
public interface TeamPlayerRepository extends JpaRepository<TeamPlayerEntity, Integer> {

    @Query(value = """
            SELECT
                player.id AS playerId,
                user.name AS userName,
                team.id AS teamId,
                team.name AS teamName
            FROM users user
                INNER JOIN users_players userPlayer ON user.id = userPlayer.user_id AND userPlayer.deleted IS NULL
                INNER JOIN players player ON userPlayer.player_id = player.id
                INNER JOIN teams_players teamPlayer ON player.id = teamPlayer.player_id AND teamPlayer.deleted IS NULL
                INNER JOIN teams team ON teamPlayer.team_id = team.id
            WHERE user.deleted IS NULL AND player.deleted IS NULL AND team.id = :pTeamId AND teamPlayer.deleted IS NULL AND teamPlayer.type_player_id = 1
                            """, nativeQuery = true)
    List<Map<String, Object>> getTitularPlayersByTeamId(Integer pTeamId);

    @Query(value = """
            SELECT
                player.id AS playerId,
                user.name AS userName,
                team.id AS teamId,
                team.name AS teamName
            FROM users user
                INNER JOIN users_players userPlayer ON user.id = userPlayer.user_id AND userPlayer.deleted IS NULL
                INNER JOIN players player ON userPlayer.player_id = player.id
                INNER JOIN teams_players teamPlayer ON player.id = teamPlayer.player_id AND teamPlayer.deleted IS NULL
                INNER JOIN teams team ON teamPlayer.team_id = team.id
            WHERE user.deleted IS NULL AND player.deleted IS NULL AND team.id = :pTeamId AND teamPlayer.deleted IS NULL AND teamPlayer.type_player_id = 2
                            """, nativeQuery = true)
    List<Map<String, Object>> getSubstitutePlayersByTeamId(Integer pTeamId);

    Optional<TeamPlayerEntity> findByTeamIdAndPlayerId(Integer teamId, Integer playerId);
}
