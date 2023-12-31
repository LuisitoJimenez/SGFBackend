package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;
//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.PlayerEntity;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Integer> {

       @Query(value = """
              SELECT
              player.id,
              user.name,
              user.id AS userId,
              gender.id AS genderId,
              JSON_UNQUOTE(JSON_EXTRACT(player.photo, '$[0].name')) AS photo,
              player.identification,
              player.birthplace,
              player.birthday,
              player.weight,
              player.height
          FROM users user
          INNER JOIN users_players userPlayer ON user.id = userPlayer.user_id AND userPlayer.deleted IS NULL
          INNER JOIN players player ON userPlayer.player_id = player.id
          INNER JOIN players_genders playerGender ON player.id = playerGender.player_id AND playerGender.deleted IS NULL
          INNER JOIN genders gender ON playerGender.gender_id = gender.id
          WHERE user.deleted IS NULL AND player.deleted IS NULL;
                                                 """, nativeQuery = true)
       List<Map<String, Object>> getListPlayers();

       @Query(value = """
                     SELECT
                            player.id,
                            user.name,
                            gender.id AS genderId,
                            player.photo,
                            player.identification,
                            player.birthplace,
                            player.birthday,
                            player.weight,
                            player.height
                     FROM users user
                              INNER JOIN users_players userPlayer ON user.id = userPlayer.user_id AND userPlayer.deleted IS NULL
                              INNER JOIN players player ON userPlayer.player_id = player.id
                              INNER JOIN players_genders playerGender ON player.id = playerGender.player_id AND playerGender.deleted IS NULL
                              INNER JOIN genders gender ON playerGender.gender_id = gender.id
                     WHERE user.deleted IS NULL AND player.id = :pPlayerId
                                    """, nativeQuery = true)
       Map<String, Object> getPlayerById(@Param("pPlayerId") Integer playerId);

}
