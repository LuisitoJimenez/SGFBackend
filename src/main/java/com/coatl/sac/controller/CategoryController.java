package com.coatl.sac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.service.CategoriesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Categories management")
public class CategoryController {

    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("/subs")
    @Operation(summary = "Get categories by age")
    public WebServiceResponse getCategoriesByAge() {
        return new WebServiceResponse(categoriesService.getSubList());
    }
}
