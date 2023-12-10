package com.coatl.sac.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TournamentDTO {
    
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String ageCategory;
    private String genderCategory; 
    private Integer userCreated;
    private Integer userDeleted;
    private LocalDateTime created;
    private LocalDateTime deleted;
    
}
