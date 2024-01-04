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
@Table(name = "player_gender")
public class PlayerGenderEntity extends BaseEntity{

    @Basic
    @Column(name = "player_id", nullable = false)
    private Integer playerId;

    @Basic
    @Column(name ="gender_id")
    private Integer genderId;

}
