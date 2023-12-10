package com.coatl.sac.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coatl.sac.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Integer userId);

    @Query(value = """
            SELECT 
                id,
                name,
                email,
                phone
            FROM users 
            WHERE deleted IS NULL
               """, nativeQuery = true)
    List<Map<String, Object>> getListOfUsers();

    @Query(value = """
            SELECT
                id,
                name,
                email,
                phone,
                active
            FROM users
            WHERE id = :pUserId
            AND deleted IS NULL
                """, nativeQuery = true)
    Map<String, Object> getUserById(@Param("pUserId") Integer userId);

    /*
     * @Query(value = """
     * SELECT user.id AS idUser,
     * user.name AS nameUser,
     * user.email,
     * user.gender,
     * user.birthday,
     * team.id AS idTeam,
     * team.name AS nameTeam,
     * team.coach
     * FROM users user
     * LEFT JOIN users_teams userteam ON user.id = userteam.user_id
     * LEFT JOIN teams team ON userteam.team_id = team.id
     * WHERE user.id = :pUserId""", nativeQuery = true)
     * List<Map<String, Object>> getUserTeams(@Param("pUserId") Integer userId);
     */

    @Query(value = """
            SELECT user.id AS idUser,
                user.name AS nameUser,
                user.email,
                user.gender,
                user.birthday,
                team.id AS idTeam,
                team.name AS nameTeam,
                team.coach
            FROM users user
                LEFT JOIN users_teams userteam ON user.id = userteam.user_id
                LEFT JOIN teams team ON userteam.team_id = team.id
            WHERE user.id = :pUserId AND userTeam.deleted IS NULL AND userTeam.team_id IS NOT NULL""", nativeQuery = true)
    List<Map<String, Object>> getTeams(@Param("pUserId") Integer userId);

    @Query(value = """
            SELECT role.id AS roleId,
                role.name AS roleName,
                team.id AS teamId,
                team.name AS teamName,
                user.id AS userId,
                user.name AS userName
            FROM roles role
                LEFT JOIN users_teams_roles userTeamRole ON role.id = userTeamRole.role_id
                LEFT JOIN users_teams userTeam ON userTeamRole.user_team_id = userTeam.id
                LEFT JOIN users user ON userTeam.user_id = user.id
                LEFT JOIN teams team ON userTeam.team_id = team.id
            WHERE user.id = :pUserId""", nativeQuery = true)
    List<Map<String, Object>> getRoles(@Param("pUserId") Integer userId);

    @Query(value = """
            SELECT CASE WHEN COUNT(user.email) > 0 THEN 'false' ELSE 'true' END AS available
            FROM users user
            WHERE user.email = :pEmail
            AND user.deleted IS NULL
            """, nativeQuery = true)
    boolean checkAvailabilityEmail(@Param("pEmail") String email);

}
