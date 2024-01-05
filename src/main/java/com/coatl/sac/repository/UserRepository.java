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
            SELECT id,
                   name,
                   email,
                   phone
            FROM user
            WHERE deleted_at IS NULL
                          """, nativeQuery = true)
    List<Map<String, Object>> getListOfUsers();

    @Query(value = """
            SELECT id,
                   name,
                   email,
                   phone
            FROM user
            WHERE id=:pUserId AND deleted_at IS NULL
                           """, nativeQuery = true)
    Map<String, Object> getUserById(@Param("pUserId") Integer userId);

    @Query(value = """
            SELECT CASE WHEN COUNT(u.email) > 0 THEN 'false' ELSE 'true' END AS available
            FROM user u
            WHERE u.email = :pEmail AND u.deleted_at IS NULL
                    """, nativeQuery = true)
    boolean checkAvailabilityEmail(@Param("pEmail") String email);

}
