package com.coatl.sac.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.Where;

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
@Table(name = "users_referees")
@Where(clause = "deleted IS NULL")
public class UserRefereeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Basic
    @Column(name = "user_Id", nullable = false)
    private Integer userId;

    @Basic
    @Column(name = "referee_Id", nullable = false)
    private Integer refereeId;

    @Basic
    @Column(name = "user_created", nullable = false)
    private Integer userCreated;

    @Basic
    @Column(name = "created" )
    private Timestamp created;

    @Basic
    @Column(name = "user_deleted")
    private Integer userDeleted;

    @Basic
    @Column(name = "deleted")
    private Timestamp deleted;
}
