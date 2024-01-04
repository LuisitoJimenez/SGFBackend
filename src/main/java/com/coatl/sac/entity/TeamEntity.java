package com.coatl.sac.entity;

import com.coatl.sac.entity.base.BaseEntity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "team")
public class TeamEntity extends BaseEntity{

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "coach", nullable = false)
    private String coach;

/*     @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "users_teams",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users; */

}
