package com.coatl.sac.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SocialNetwork {
    
    private String facebook;
    private String twitter;
    private String tiktok;
    private String instagram;
    private String youtube;
    private String other;
    private String website;

}
