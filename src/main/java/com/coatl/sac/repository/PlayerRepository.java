package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.PlayerEntity;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Integer> {

       @Query(value = """
                     SELECT p.id,
                            user.name,
                            user.id                                          AS userId,
                            g.id                                             AS genderId,
                            JSON_UNQUOTE(JSON_EXTRACT(p.photo, '$[0].name')) AS photo,
                            p.identification,
                            p.birthplace,
                            p.birthday,
                            p.weight,
                            p.height
                     FROM user user
                              INNER JOIN user_player up ON user.id = up.user_id AND up.deleted_at IS NULL
                              INNER JOIN player p ON up.player_id = p.id
                              INNER JOIN player_gender pg ON p.id = pg.player_id AND pg.deleted_at IS NULL
                              INNER JOIN gender g ON pg.gender_id = g.id
                     WHERE user.deleted_at IS NULL
                       AND p.deleted_at IS NULL
                     """, nativeQuery = true)
       List<Map<String, Object>> getListPlayers();

       @Query(value = """
                     SELECT p.id,
                            u.name,
                            g.id AS genderId,
                            p.photo,
                            p.identification,
                            p.birthplace,
                            p.birthday,
                            p.weight,
                            p.height
                     FROM user u
                              INNER JOIN user_player up ON u.id = up.user_id AND up.deleted_at IS NULL
                              INNER JOIN player p ON up.player_id = p.id
                              INNER JOIN player_gender pg ON p.id = pg.player_id AND pg.deleted_at IS NULL
                              INNER JOIN gender g ON pg.gender_id = g.id
                     WHERE u.deleted_at IS NULL
                       AND p.id = :pPlayerId
                                                  """, nativeQuery = true)
       Map<String, Object> getPlayerById(@Param("pPlayerId") Integer playerId);

}
