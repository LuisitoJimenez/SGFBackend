package com.coatl.sac.entity;

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
@Table(name = "statistics")
public class StatisticEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Basic
    @Column(name = "name_game")
    private String nameGame;

    @Basic
    @Column(name = "goals")
    private Integer goals;

    @Basic
    @Column(name = "yellow_cards")
    private Integer yellowCards;

    @Basic
    @Column(name = "red_cards")
    private Integer redCards;

    @Basic
    @Column(name = "penalties")
    private Integer penalties;

}
