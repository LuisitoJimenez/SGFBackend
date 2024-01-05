package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.TournamentEntity;

@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, Integer> {
    
    List<TournamentEntity> findAll();

    /* @Query(
        value = """
            SELECT
            tournament.id,
            tournament.name,
            tournament.age_category,
            tournament.gender_category,
            tournament.start_date,
            tournament.end_date,
            game.id,
            game.name,
            game.date_time,
            game.referee
        FROM tournaments tournament
            LEFT JOIN tournaments_games tournamentGame ON tournament.id = tournamentGame.tournament_id
            LEFT JOIN games game ON tournamentGame.game_id = game.id
        WHERE tournament.id = :ptournamentId AND tournamentGame.deleted IS NULL
                """, nativeQuery = true
    )List<Map<String, Object>> getTournamentGames(@Param("ptournamentId") Integer tournamentId);
 */
}