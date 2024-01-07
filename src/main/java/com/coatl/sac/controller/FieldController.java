package com.coatl.sac.controller;

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
            @RequestBody FieldDTO fieldDTO) {
        return fieldService.createField(fieldDTO);
    }

    @GetMapping("")
    @Operation(summary = "Get all fields")
    public WebServiceResponse getAllFields() {
        return new WebServiceResponse(fieldService.getFieldsList());
    }

    @GetMapping("/{fieldId}")
    @Operation(summary = "Get field by id")
    public WebServiceResponse getFieldById(
            @PathVariable Integer fieldId) {
        return new WebServiceResponse(fieldService.getFieldById(fieldId));
    }

    @DeleteMapping("/{fieldId}")
    @Operation(summary = "Delete field")
    public WebServiceResponse deletedField(
            @PathVariable Integer fieldId) {
        return fieldService.deleteField(fieldId);
    }

    @PatchMapping("/{fieldId}")
    @Operation(summary = "Update field")
    public WebServiceResponse updateField(
            @PathVariable Integer fieldId,
            @RequestBody FieldDTO fieldDTO) {
        return fieldService.updateField(fieldDTO, fieldId);
    }

    @PostMapping(value = "/{fieldId}")
    @Operation(summary = "Save image field")
    public WebServiceResponse saveImagePlayer(
            @PathVariable Integer fieldId,
            @RequestParam() MultipartFile file) {
        return fieldService.saveImageField(fieldId, file);
    }

    @GetMapping("/{fieldId}/image/{fileId}")
    @Operation(summary = "Get image field")
    public byte[] getImagePlayer(
            @PathVariable Integer fieldId,
            @PathVariable String fileId) {
        return fieldService.getImageField(fieldId, fileId);
    }

}
