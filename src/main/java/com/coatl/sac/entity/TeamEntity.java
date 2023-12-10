package com.coatl.sac.entity;

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

}
