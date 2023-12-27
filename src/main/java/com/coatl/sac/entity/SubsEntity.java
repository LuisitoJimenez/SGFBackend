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
@Table(name = "subs")
public class SubsEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true )
    private Integer id;

    @Basic
    @JsonIgnore
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @JsonIgnore
    @Column(name = "min_age", nullable = false)
    private Integer minAge;

    @Basic
    @JsonIgnore
    @Column(name = "max_age", nullable = false)
    private Integer maxAge;


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
