package com.coatl.sac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.model.TournamentDTO;
import com.coatl.sac.service.TournamentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tournaments")
@Tag(name = "Tournaments", description = "Tournaments management")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping("")
    @Operation(summary = "Create tournament")
    public WebServiceResponse createTournament(
        @RequestBody TournamentDTO tournamentDto
    ) {
        return tournamentService.createTournament(tournamentDto); 
    }
    
    @GetMapping("")
    @Operation(summary = "Get tournament list")
    public WebServiceResponse getTournamentList() {
        return new WebServiceResponse(tournamentService.getTournamentList());
    }

    @PostMapping("/{tournamentId}/game/{gameId}")
    @Operation(summary = "Add game to tournament")
    public WebServiceResponse addTeamToTournament(
        @PathVariable Integer tournamentId,
        @PathVariable Integer gameId
    ) {
        return tournamentService.addTeamToTournament(tournamentId, gameId);
    }

/*     @GetMapping("/{tournamentId}/games")
    public WebServiceResponse getTournamentGames(
        @PathVariable Integer tournamentId
    ) {
        return new WebServiceResponse(tournamentService.getTournamentGames(tournamentId));
    } */

}
