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
@Table(name = "genders")
public class GenderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Basic
    @JsonIgnore
    @Column(name = "user_created")
    private Integer userCreated;
    
    @Basic    
    @JsonIgnore
    @Column(name = "created")
    private Timestamp created;

    @Basic
    @JsonIgnore
    @Column(name = "user_deleted")
    private Integer userDeleted;

    @Basic
    @JsonIgnore
    @Column(name = "deleted")
    private Timestamp deleted;
}
