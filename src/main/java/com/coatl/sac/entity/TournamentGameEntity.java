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

@Getter
@Setter
@Entity
@Table(name = "tournaments_games")
public class TournamentGameEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Basic
    @Column(name = "tournament_id", nullable = false)
    private Integer tournamentId;

    @Basic
    @Column(name = "game_id", nullable = false)
    private Integer gameId;

    @Basic
    @Column(name = "user_created", nullable = false)
    private Integer userCreated;

    @Basic
    @Column(name = "created" )
    private Timestamp created;

    @Basic
    @Column(name = "user_deleted")
    private Integer userDeleted;

    @Basic
    @Column(name = "deleted")
    private Timestamp deleted;
    
}
