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
@Table(name = "matchday")
public class MatchdayEntity extends BaseEntity {
    
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
    
}
