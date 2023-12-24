package com.coatl.sac.entity;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.Type;

import com.coatl.sac.json.UserName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "teams")
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;
    
    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @Type(JsonType.class)
    @Column(name = "coach", columnDefinition ="longtext")
    private UserName coach;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "users_teams",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users;

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
