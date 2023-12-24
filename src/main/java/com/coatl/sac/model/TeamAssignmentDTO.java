package com.coatl.sac.model;

import java.util.List;

import lombok.Data;

@Data
public class TeamAssignmentDTO {
    
    private Integer teamId;
    private List<Integer> playersIds;
}
