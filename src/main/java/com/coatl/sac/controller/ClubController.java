package com.coatl.sac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.model.ClubDTO;
import com.coatl.sac.service.ClubService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/clubs")
@Tag(name = "Club", description = "Club management")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @GetMapping("")
    @Operation(summary = "Get club list")
    public WebServiceResponse getClubsList() {
        return new WebServiceResponse(clubService.getClubsList());
    }

    @PostMapping("")
    @Operation(summary = "Create club")
    public WebServiceResponse createClub(
        @RequestBody ClubDTO clubDto
    ) {
        return new WebServiceResponse(clubService.createClub(clubDto));
    }

    @PatchMapping("/{clubId}")
    @Operation(summary = "Update club")
    public WebServiceResponse updateClub(
        @PathVariable Integer clubId,
        @RequestBody ClubDTO clubDto
    ) {
        return clubService.updateClub(clubDto, clubId);
    }

    @GetMapping("/{clubId}")
    @Operation(summary = "Get club by id")
    public WebServiceResponse getClubId(
        @PathVariable Integer clubId
    ) {
        return new WebServiceResponse(clubService.getClubById(clubId));
    }

    @DeleteMapping("/{clubId}")
    @Operation(summary = "Delete club by id") 
    public WebServiceResponse deleteClub(
        @PathVariable Integer clubId
    ){
       return clubService.deleteClub(clubId); 
    }

    @PostMapping(value = "/{clubId}")
    public WebServiceResponse saveImagePlayer(
        @PathVariable Integer clubId,
        @RequestParam() MultipartFile file
    ) {
        return clubService.saveImageClub(clubId, file);
    }

    @GetMapping("/{clubId}/image/{fileId}")
    public byte[] getImagePlayer(
        @PathVariable Integer clubId,
        @PathVariable String fileId
    ) {
        return clubService.getImageClub(clubId, fileId);
    }
}
