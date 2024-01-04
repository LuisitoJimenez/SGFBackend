package com.coatl.sac.controller;

import java.util.Locale;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.model.FieldDTO;
import com.coatl.sac.service.FieldService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fields")
@Tag(name = "Fields", description = "Field management")
public class FieldController {

    private final FieldService fieldService;
    
    @PostMapping("")
    @Operation(summary = "Create field")
    public WebServiceResponse createField(
        @RequestBody FieldDTO fieldDTO
    ) {
        return new WebServiceResponse(fieldService.createField(fieldDTO));
    }
    
}
