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
@Table(name = "referee_gender")
public class RefereeGenderEntity extends BaseEntity {

    @Basic
    @Column(name = "referee_id", nullable = false)
    private Integer refereeId;

    @Basic
    @Column(name = "gender_id")
    private Integer genderId;

}
