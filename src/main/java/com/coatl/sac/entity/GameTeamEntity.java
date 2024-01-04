package com.coatl.sac.entity;

import com.coatl.sac.entity.base.BaseEntity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "game_team")
public class GameTeamEntity extends BaseEntity{
    
    @Basic
    @Column(name = "game_id", nullable = false)
    private Integer gameId;

    @Basic
    @Column(name = "team_id", nullable = false)
    private Integer teamId;
     
}
