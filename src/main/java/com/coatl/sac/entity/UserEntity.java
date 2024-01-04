package com.coatl.sac.entity;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import com.coatl.sac.entity.base.BaseEntity;
import com.coatl.sac.json.UserName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity{

    @NaturalId
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Column(name = "external_id", nullable = false)
    private String externalId;
    
    @Type(JsonType.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Column(name = "name", columnDefinition = "longtext")
    private UserName name;

    @Basic
    @JsonIgnore
    @Column(name = "email", nullable = false)
    private String email;

    @Basic
    @JsonIgnore
    @Column(name = "phone", nullable = false)
    private String phone;

}
