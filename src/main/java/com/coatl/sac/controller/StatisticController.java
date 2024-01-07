package com.coatl.sac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.model.StatisticDTO;
import com.coatl.sac.service.StatisticService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/statistics")
@Tag(name = "Statistics", description = "Statistic management")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;
    
    @PostMapping("")
    @Operation(summary = "Create statistic")
    public WebServiceResponse createStatistic(
        @RequestBody StatisticDTO statisticDto
    ) {
        return statisticService.createStatistic(statisticDto);
    }

/*     @GetMapping("/{gameId}")
    public WebServiceResponse getStatisticByGame(
        @PathVariable Integer gameId
    ) {
        return new WebServiceResponse(statisticService.getStatisticByGame(gameId));
    } */
    
}
