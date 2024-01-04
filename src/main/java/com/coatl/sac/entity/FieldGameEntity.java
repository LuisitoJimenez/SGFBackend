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
@Table(name = "field_game")
public class FieldGameEntity extends BaseEntity{

    @Column(name = "field_id", nullable = false)
    private Integer fieldId;
    
    @Column(name = "game_id", nullable = false)
    private Integer gameId;

}
