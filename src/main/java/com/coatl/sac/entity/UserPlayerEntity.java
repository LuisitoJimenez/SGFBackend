package com.coatl.sac.entity;

import org.hibernate.annotations.Where;

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
@Table(name = "user_player")
@Where(clause = "deleted IS NULL")
public class UserPlayerEntity extends BaseEntity{

    @Basic
    @Column(name = "user_Id", nullable = false)
    private Integer userId;

    @Basic
    @Column(name = "player_Id", nullable = false)
    private Integer playerId;

}
