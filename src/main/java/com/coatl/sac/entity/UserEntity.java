package com.coatl.sac.entity;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

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
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

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

    /* @Basic
    @Column(name = "birthday")
    @JsonIgnore
    private Date birthday; */

    /* @Type(JsonType.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Column(name = "address", columnDefinition = "longtext")
    private Address address;
 */
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

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "users_teams",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<TeamEntity> teams;
    
}
