package com.coatl.sac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.model.TeamAssignmentDTO;
import com.coatl.sac.model.TeamDTO;
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

    @PostMapping("")
    @Operation(summary = "Create team")
    public WebServiceResponse createTeam (
       @RequestBody TeamDTO teamDto,
       @RequestHeader Integer clubId
    ) {
        return new WebServiceResponse(teamService.createTeam(teamDto, clubId));
    }

    @PostMapping("/titulars")
    @Operation(summary = "Assign players to team")
    public WebServiceResponse assignPlayersToTeam (
       @RequestBody TeamAssignmentDTO teamDto
    ) {
        return teamService.addTitularPlayerToTeam(teamDto);
    }

    @PostMapping("/substitutes")
    @Operation(summary = "Assign substitutes to team")
    public WebServiceResponse assignSubstitutesToTeam (
       @RequestBody TeamAssignmentDTO teamDto
    ) {
        return teamService.addSubstitutePlayerToTeam(teamDto);
    }

    @GetMapping("/{teamId}/titulars")
    @Operation(summary = "Get titulars")
    public WebServiceResponse getTitularsPlayers (
       @PathVariable Integer teamId
    ) {
        return new WebServiceResponse(teamService.getTitularsPlayers(teamId));
    }

    @GetMapping("/{teamId}/substitutes")
    @Operation(summary = "Get substitutes")
    public WebServiceResponse getSubstitutesPlayers (
       @PathVariable Integer teamId
    ) {
        return new WebServiceResponse(teamService.getSubstitutesPlayers(teamId));
    }

    @GetMapping("/{teamId}")
    @Operation(summary = "Get team by id")
    public WebServiceResponse getTeamById (
       @PathVariable Integer teamId
    ) {
        return new WebServiceResponse(teamService.getTeamById(teamId));
    }

    @DeleteMapping("/{teamId}")
    @Operation(summary = "Delete team by id")
    public WebServiceResponse deleteTeamById (
       @PathVariable Integer teamId
    ) {
        return teamService.deleteTeamById(teamId);
    }

}
