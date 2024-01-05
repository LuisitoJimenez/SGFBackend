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
@Table(name = "user_referee")
public class UserRefereeEntity extends BaseEntity{

    @Basic
    @Column(name = "user_Id", nullable = false)
    private Integer userId;

    @Basic
    @Column(name = "referee_Id", nullable = false)
    private Integer refereeId;

}
