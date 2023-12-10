package com.coatl.sac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.model.GameDTO;
import com.coatl.sac.service.GameService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/games")
@Tag(name = "Games", description = "Games management")
public class GameController {
    
    @Autowired
    private GameService gameService;

    @GetMapping("")
    @Operation(summary = "Get game list")
    public WebServiceResponse getGameList() {
        return new WebServiceResponse(gameService.getGameList());
    }

    @PostMapping("")
    public WebServiceResponse createGame(
        @RequestBody GameDTO gameDto
    ) {
        return gameService.createGame(gameDto); 
    }

    @PostMapping("/{gameId}/team/{teamId}")
    public WebServiceResponse assignGameTeam (
        @PathVariable Integer gameId,
        @PathVariable Integer teamId
    ) {
        return gameService.assignGameTeam(gameId, teamId);
    }

    @GetMapping("/{gameId}/teams")
    public WebServiceResponse getGameTeams (
        @PathVariable Integer gameId
    ) {
        return new WebServiceResponse(gameService.getGameTeams(gameId));
    }

}
