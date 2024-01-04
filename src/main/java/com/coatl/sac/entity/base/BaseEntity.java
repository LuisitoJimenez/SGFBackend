package com.coatl.sac.entity.base;

import java.sql.Timestamp;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Basic
    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Basic
    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Basic
    @Column(name = "deleted_by")
    private Integer deletedBy;

}
