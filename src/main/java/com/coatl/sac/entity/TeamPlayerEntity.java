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
@Table(name = "team_player")
public class TeamPlayerEntity extends BaseEntity{

    @Basic
    @Column(name = "team_id", nullable = false)
    private Integer teamId;

    @Basic
    @Column(name = "player_Id", nullable = false)
    private Integer playerId;

    @Basic
    @Column(name = "position_id")
    private Integer positionId;
    
    @Basic
    @Column(name = "type_player_id", nullable = false)
    private Integer typePlayerId;

}
