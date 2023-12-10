package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.RefereeEntity;

@Repository
public interface RefereeRepository extends JpaRepository<RefereeEntity, Integer> {

       @Query(value = """
                     SELECT
                         referee.id,
                         user.name,
                         user.id AS userId,
                         gender.id AS genderId,
                         JSON_UNQUOTE(JSON_EXTRACT(referee.photo, '$[0].name')) AS photo,
                         referee.identification,
                         referee.birthplace,
                         referee.birthday,
                         referee.weight,
                         referee.height
                     FROM users user
                     INNER JOIN users_referees userReferee ON user.id = userReferee.user_id AND userReferee.deleted IS NULL
                     INNER JOIN referees referee ON userReferee.referee_id = referee.id
                     INNER JOIN referees_genders refereeGender ON referee.id = refereeGender.referee_id AND refereeGender.deleted IS NULL
                     INNER JOIN genders gender ON refereeGender.gender_id = gender.id
                     WHERE user.deleted IS NULL AND referee.deleted IS NULL;
                                                    """, nativeQuery = true)
       List<Map<String, Object>> getRefereesList();

       @Query(value = """
                     SELECT
                            referee.id,
                            user.name,
                            gender.id AS genderId,
                            referee.photo,
                            referee.identification,
                            referee.birthplace,
                            referee.birthday,
                            referee.weight,
                            referee.height
                     FROM users user
                              INNER JOIN users_referees userReferee ON user.id = userReferee.user_id AND userReferee.deleted IS NULL
                              INNER JOIN referees referee ON userReferee.referee_id = referee.id
                              INNER JOIN referees_genders refereeGender ON referee.id = refereeGender.referee_id AND refereeGender.deleted IS NULL
                              INNER JOIN genders gender ON refereeGender.gender_id = gender.id
                     WHERE user.deleted IS NULL AND referee.id = :pRefereeId
                     """, nativeQuery = true)
       Map<String, Object> getRefereeById(@Param("pRefereeId") Integer playerId);
}
