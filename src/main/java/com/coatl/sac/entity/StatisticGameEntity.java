package com.coatl.sac.entity;

import java.sql.Timestamp;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "statistics_games")
public class StatisticGameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Basic
    @Column(name = "statistic_id", nullable = false)
    private Integer statisticId;

    @Basic
    @Column(name = "game_id", nullable = false)
    private Integer gameId;

    @Basic
    @Column(name = "user_created", nullable = false)
    private Integer userCreated;

    @Basic
    @Column(name = "created")
    private Timestamp created;

    @Basic
    @Column(name = "user_deleted")
    private Integer userDeleted;

    @Basic
    @Column(name = "deleted")
    private Timestamp deleted;
}
