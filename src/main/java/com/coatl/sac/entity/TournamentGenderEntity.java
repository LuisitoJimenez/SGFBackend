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
@Table(name = "tournament_gender")
public class TournamentGenderEntity extends BaseEntity{
    
    @Column(name = "tournament_id", nullable = false)
    private Integer tournamentId;

    @Column(name = "gender_id", nullable = false)
    private Integer genderId;

}
