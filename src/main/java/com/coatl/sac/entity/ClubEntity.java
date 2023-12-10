package com.coatl.sac.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.Type;

import com.coatl.sac.json.SocialNetwork;
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
@Table(name = "clubs")
public class ClubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

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
