package com.coatl.sac.entity;

import com.coatl.sac.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sub")
public class SubEntity extends BaseEntity{

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

}
