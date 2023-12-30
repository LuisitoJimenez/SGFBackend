package com.coatl.sac.model;

//import java.time.LocalDate;

import lombok.Data;

@Data
public class UserDTO {

    private String first;
    private String last;
    private String secondLast;
    private String email;
    private String phone;
    //private String street;
    //private String postalCode;
    //private String municipality;
    //private String town;
    //private String state;
    //private String gender;
    //private LocalDate birthday;
    private String password;
    
}
