package com.coatl.sac.entity;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.Type;

import com.coatl.sac.json.Birthplace;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.hypersistence.utils.hibernate.type.json.JsonType;
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
@Table(name = "players")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @JsonIgnore
    @Column(name = "photo")
    private String photo;

    @Basic
    @JsonIgnore
    @Column(name = "identification")
    private String identification;

    @Basic
    @Type(JsonType.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Column(name = "birthplace", columnDefinition = "longtext")
    private Birthplace birthplace;

    @Basic
    @Column(name = "birthday")
    @JsonIgnore
    private Date birthday;

    @Basic
    @JsonIgnore
    @Column (name = "weight")
    private Float weight;

    @Basic
    @JsonIgnore
    @Column (name = "height")
    private Float height;

/*     @Basic
    @JsonIgnore
    @Column(name = "nationality")
    private String nationality; */

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
