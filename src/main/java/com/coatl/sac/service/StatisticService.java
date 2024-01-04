package com.coatl.sac.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.StatisticEntity;
import com.coatl.sac.model.StatisticDTO;
import com.coatl.sac.repository.StatisticRepository;

import jakarta.transaction.Transactional;

@Service
public class StatisticService {

    @Autowired
    private StatisticRepository statisticRepository;

    @Transactional
    public WebServiceResponse createStatistic(StatisticDTO statisticDto) {
            StatisticEntity statisticEntity = new StatisticEntity();
            statisticEntity.setRedCards(statisticDto.getRedCards());
            statisticEntity.setYellowCards(statisticDto.getYellowCards());
            statisticEntity.setGoals(statisticDto.getGoals());
            statisticRepository.save(statisticEntity);
            return new WebServiceResponse(true, "Statistic created successfully");
    }


    public List<Map<String, Object>> getStatisticByGame(Integer gameId) {
        List<Map<String, Object>> statistic = statisticRepository.getStatisticByGame(gameId);
        return statistic; 
    }

    
}
