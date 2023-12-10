package com.coatl.sac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.service.TeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/teams")
@Tag(name = "Teams", description = "Teams management")
public class TeamController {
    
    @Autowired
    private TeamService teamService;

    @GetMapping("")
    @Operation(summary = "Get team list")
    public WebServiceResponse getTeamList() {
        return new WebServiceResponse(teamService.getTeamList());
    }
}
