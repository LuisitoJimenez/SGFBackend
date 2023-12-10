package com.coatl.sac.entity;

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
@Table(name = "users_teams_roles")
public class UserTeamRoleEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Basic
    @Column(name = "role_id")
    private Integer roleId;

    @Basic
    @Column(name = "user_team_id")
    private Integer userTeamId;
    
    @Basic
    @Column(name = "User_created")
    private Integer userCreated;
    
}
