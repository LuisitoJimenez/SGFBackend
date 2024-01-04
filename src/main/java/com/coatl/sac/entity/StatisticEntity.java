package com.coatl.sac.entity;

import com.coatl.sac.entity.base.BaseEntity;

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
@Table(name = "statistic")
public class StatisticEntity extends BaseEntity {

    @Column(name = "goals")
    private Integer goals;

    @Column(name = "yellow_cards")
    private Integer yellowCards;

    @Column(name = "red_cards")
    private Integer redCards;

}
