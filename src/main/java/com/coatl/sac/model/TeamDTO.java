package com.coatl.sac.model;

import com.coatl.sac.json.UserName;

import lombok.Data;

@Data
public class TeamDTO {

    private String name;
    private Integer genderId;
    private Integer subId;
    private UserName coach;

}
