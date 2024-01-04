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
@Table(name = "matchday_game")
public class MatchdayGameEntity extends BaseEntity {
    
    @Column(name = "matchday_id", nullable = false)
    private Integer matchdayId;

    @Column(name ="game_id", nullable = false)
    private Integer gameId;
    
}
