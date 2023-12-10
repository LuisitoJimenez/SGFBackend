package com.coatl.sac.model;
import java.time.LocalDate;

import lombok.Data;

@Data
public class PlayerDTO {
    
    private String photo;
    private String identification;
    private Integer gender;
    private LocalDate birthday;
    private Float height;
    private Float weight;
    private String state;
    private String town;

}
