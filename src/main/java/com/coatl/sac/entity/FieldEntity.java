package com.coatl.sac.entity;

import org.hibernate.annotations.Type;

import com.coatl.sac.entity.base.BaseEntity;
import com.coatl.sac.json.Address;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "field")
public class FieldEntity extends BaseEntity{
    
    @Column(name = "logo", columnDefinition = "longtext")
    private String logo;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Type(JsonType.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Column(name = "address")
    private Address address;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    
}
