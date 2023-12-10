package com.coatl.sac.model;

import lombok.Data;

@Data
public class StatisticDTO {

    private String nameGame;
    private Integer yellowCards;
    private Integer redCards;
    private Integer goals;
    private Integer penalties;
    
}
