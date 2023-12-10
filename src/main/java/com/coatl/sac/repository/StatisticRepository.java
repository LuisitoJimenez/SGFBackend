package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.StatisticEntity;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticEntity, Integer> {

    boolean existsByNameGame(String nameGame);

    @Query(value = """
            SELECT statistic.name_game,
                statistic.id,
                statistic.goals,
                statistic.yellow_cards,
                statistic.red_cards,
                statistic.penalties
            FROM statistics AS statistic
            LEFT JOIN statistics_games sg ON statistic.id = sg.statistic_id
            LEFT JOIN games g ON sg.game_id = g.id
            WHERE g.id = :pGameId
                        """, nativeQuery = true)
    List<Map<String, Object>> getStatisticByGame(@Param ("pGameId") Integer gameId);

}
