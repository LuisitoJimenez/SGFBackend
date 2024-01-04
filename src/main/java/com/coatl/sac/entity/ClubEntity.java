package com.coatl.sac.entity;

import org.hibernate.annotations.Type;

import com.coatl.sac.entity.base.BaseEntity;
import com.coatl.sac.json.SocialNetwork;
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
@Table(name = "club")
public class ClubEntity extends BaseEntity{

    @Basic
    @Column(name = "logo", columnDefinition = "longtext")
    private String logo;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Type(JsonType.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Column(name = "social_networks", columnDefinition = "longtext")
    private SocialNetwork socialNetworks;

    @Basic
    @JsonIgnore
    @Column(name = "email", nullable = false)
    private String email;

    @Basic
    @JsonIgnore
    @Column(name = "phone", nullable = false)
    private String phone;

}
