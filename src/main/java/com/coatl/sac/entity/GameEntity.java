package com.coatl.sac.entity;

import java.sql.Date;
import java.sql.Time;

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
@Table(name = "game")
public class GameEntity extends BaseEntity{

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "game_time", nullable = false)
    private Time gameTime;

    @Basic
    @Column(name = "game_date", nullable = false)
    private Date gameDate;

}
