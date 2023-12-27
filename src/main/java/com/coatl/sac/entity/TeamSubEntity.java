package com.coatl.sac.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "teams_subs")
public class TeamSubEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "sub_id", nullable = false)
    private Integer subId;

    @Basic
    @Column(name = "team_id", nullable = false)
    private Integer teamId;
 
    @Basic
    @Column(name = "user_created")
    @JsonIgnore
    private Integer userCreated;
    
    @Basic
    @Column(name = "created")
    @JsonIgnore
    private Timestamp created;

    @Basic
    @Column(name = "user_deleted")
    @JsonIgnore
    private Integer userDeleted;

    @Basic
    @Column(name = "deleted")
    @JsonIgnore
    private Timestamp deleted;
}
