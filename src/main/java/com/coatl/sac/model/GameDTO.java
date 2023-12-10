package com.coatl.sac.model;

import java.time.LocalDateTime;

import com.coatl.sac.json.UserName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDTO {

    private String name;
    @JsonProperty("referee")
    private UserName referee;
    private LocalDateTime gameDateTime;
    private Integer userCreated;
    private Integer userDeleted;
    private LocalDateTime created;
    private LocalDateTime deleted;

}
