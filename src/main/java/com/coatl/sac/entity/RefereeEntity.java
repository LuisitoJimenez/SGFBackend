package com.coatl.sac.entity;

import java.sql.Date;

import org.hibernate.annotations.Type;

import com.coatl.sac.entity.base.BaseEntity;
import com.coatl.sac.json.Birthplace;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "referee")
public class RefereeEntity extends BaseEntity{
    
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

}
