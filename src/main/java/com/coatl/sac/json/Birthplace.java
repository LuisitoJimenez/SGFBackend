package com.coatl.sac.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Birthplace {
    private String state;
    private String town;
}
