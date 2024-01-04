package com.coatl.sac.model;

import lombok.Data;

@Data
public class FieldDTO {

    private String name;
    private String logo;
    private String phone;
    private String email;
    private String street;
    private String postalCode;
    private String municipality;
    private String town;
    private String state;
    private Integer capacity;

}
