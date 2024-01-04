package com.coatl.sac.entity;

import com.coatl.sac.entity.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "game_team_statistic")
public class GameTeamStatisticEntity extends BaseEntity{
    
    @Column(name = "game_id", nullable = false)
    private Integer gameId;

    @Column(name = "team_statistic_id", nullable = false)
    private Integer teamStatisticId;

}
