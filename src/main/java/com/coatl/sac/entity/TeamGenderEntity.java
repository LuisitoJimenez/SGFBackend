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
@Table(name = "team_gender")
public class TeamGenderEntity extends BaseEntity{
    
    @Basic
    @Column(name = "team_id", nullable = false)
    private Integer teamId;

    @Basic
    @Column(name = "gender_id", nullable = false)
    private Integer genderId;


}
