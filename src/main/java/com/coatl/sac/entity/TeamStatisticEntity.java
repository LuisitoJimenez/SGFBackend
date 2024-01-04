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
@Table(name = "team_statistic")
public class TeamStatisticEntity extends BaseEntity{
    
    @Column(name = "team_id", nullable = false)
    private Integer teamId;

    @Column(name = "statistic_id", nullable = false)
    private Integer statisticId;
    
}
