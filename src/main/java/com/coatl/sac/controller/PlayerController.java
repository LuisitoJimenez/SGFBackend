package com.coatl.sac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.model.PlayerDTO;
import com.coatl.sac.service.PlayerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/players")
@Tag(name = "Players", description = "Players management")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("")
    @Operation(summary = "Get all players")
    public WebServiceResponse getPlayerList() {
        return new WebServiceResponse(playerService.getPlayerList());
    }

    @GetMapping("/{playerId}")
    @Operation(summary = "Get player by id")
    public WebServiceResponse getPlayerById(
            @PathVariable Integer playerId) {
        return new WebServiceResponse(playerService.getPlayerById(playerId));
    }

    @PostMapping("")
    @Operation(summary = "Create player")
    public WebServiceResponse createPlayer(
            @RequestHeader Integer userId,
            @RequestBody PlayerDTO playerDto) {

        return new WebServiceResponse(playerService.createPlayer(playerDto, userId));
    }

    @DeleteMapping("")
    public WebServiceResponse deletePlayer(
            @RequestHeader Integer playerId) {
        return playerService.deletePlayer(playerId);
    }

    @PatchMapping("") 
    public WebServiceResponse updatePlayer(
            @RequestHeader Integer playerId,
            @RequestBody PlayerDTO playerDto
    ) {
        return playerService.updatePlayer(playerDto, playerId);
    }

    @PostMapping(value ="/{playerId}")
    public WebServiceResponse saveImagePlayer(
        @PathVariable Integer playerId,
        @RequestParam() MultipartFile file
    ) {
        return playerService.saveImagePlayer(playerId, file);
    }

    @GetMapping("/{playerId}/image/{fileId}") 
    public byte[] getImagePlayer(
        @PathVariable Integer playerId,
        @PathVariable String fileId
    ){
        return playerService.getImagePlayer(playerId, fileId);
    }

}
