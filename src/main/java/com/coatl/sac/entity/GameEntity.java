package com.coatl.sac.entity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

//import org.hibernate.annotations.Type;

//import com.coatl.sac.json.UserName;
import com.fasterxml.jackson.annotation.JsonIgnore;

//import io.hypersistence.utils.hibernate.type.json.JsonType;
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
@Table(name = "games")
public class GameEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "game_time", nullable = false)
    private Time gameTime;

    @Basic
    @Column(name = "game_date", nullable = false)
    private Date gameDate;

    @Basic
    @Column(name = "field", nullable = false)
    private String field;
/*     @JsonIgnore
    @Type(JsonType.class)
    @Column(name = "referee", columnDefinition = "longtext")
    private UserName referee; */

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
