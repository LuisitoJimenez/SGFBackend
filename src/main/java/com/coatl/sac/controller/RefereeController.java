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
import com.coatl.sac.model.RefereeDTO;
import com.coatl.sac.service.RefereeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/referees")
@Tag(name = "Referees", description = "Referee management")
public class RefereeController {

    @Autowired
    private RefereeService refereeService;

    @GetMapping("")
    @Operation(summary = "Get all referees")
    public WebServiceResponse getRefereeList() {
        return new WebServiceResponse(refereeService.getRefereesList());
    }

    @PostMapping("/{userId}")
    @Operation(summary = "Create a new referee")
    public WebServiceResponse createReferee(
        @PathVariable Integer userId,
        @RequestBody RefereeDTO refereeDto) {
        return new WebServiceResponse(refereeService.createReferee(refereeDto, userId));
    }

    @GetMapping("/{refereeId}")
    @Operation(summary = "Get referee by id")
    public WebServiceResponse getRefereeById(
        @PathVariable Integer refereeId) {
        return new WebServiceResponse(refereeService.getRefereeById(refereeId));
    }

    @DeleteMapping("/{refereeId}")
    @Operation(summary = "Delete referee by id")
    public WebServiceResponse deleteReferee(
        @PathVariable Integer refereeId) {
        return refereeService.deleteReferee(refereeId);
    }

    @PatchMapping("/{refereeId}")
    public WebServiceResponse updateReferee(
        @PathVariable Integer refereeId,
        @RequestBody RefereeDTO refereeDto) {
        return refereeService.updateReferee(refereeDto, refereeId);
    }

    
    @PostMapping(value ="/{refereeId}/image")
    public WebServiceResponse saveImageReferee(
        @PathVariable Integer refereeId,
        @RequestParam() MultipartFile file
    ) {
        return refereeService.saveImageReferee(refereeId, file);
    }

    @GetMapping("/{refereeId}/image/{fileId}") 
    public byte[] getImageReferee(
        @PathVariable Integer refereeId,
        @PathVariable String fileId
    ) {
        return refereeService.getImageReferee(refereeId, fileId);
    }

}
