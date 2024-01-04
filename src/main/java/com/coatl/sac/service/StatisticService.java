package com.coatl.sac.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coatl.sac.dto.WebServiceResponse;
//import com.coatl.sac.entity.GameEntity;
import com.coatl.sac.entity.StatisticEntity;
import com.coatl.sac.entity.StatisticGameEntity;
import com.coatl.sac.model.StatisticDTO;
import com.coatl.sac.repository.GameRepository;
import com.coatl.sac.repository.StatisticGameRepository;
import com.coatl.sac.repository.StatisticRepository;

import jakarta.transaction.Transactional;

@Service
public class StatisticService {

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private StatisticGameRepository statisticGameRepository;

    @Transactional
    public WebServiceResponse createStatistic(StatisticDTO statisticDto) {
/*         if (statisticRepository.existsByNameGame(statisticDto.getNameGame())) {
            return new WebServiceResponse(false, "There is a statistic with that name");
        } else { */
            StatisticEntity statisticEntity = new StatisticEntity();
            //statisticEntity.setNameGame(statisticDto.getNameGame());
            statisticEntity.setRedCards(statisticDto.getRedCards());
            statisticEntity.setYellowCards(statisticDto.getYellowCards());
            statisticEntity.setGoals(statisticDto.getGoals());
            //statisticEntity.setPenalties(statisticDto.getPenalties());
            statisticRepository.save(statisticEntity);
            return new WebServiceResponse(true, "Statistic created successfully");
        //}
    }

    @Transactional
    public WebServiceResponse assignStatisticGame(Integer statisticId, Integer gameId) {
        /* StatisticEntity statisticEntity =  */statisticRepository.findById(statisticId)
                .orElseThrow(() -> new RuntimeException("Statistic not found"));
       /*  GameEntity gameEntity =  */gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        StatisticGameEntity statisticGameEntity = new StatisticGameEntity();
        statisticGameEntity.setStatisticId(statisticId);
        statisticGameEntity.setGameId(gameId);
        statisticGameEntity.setUserCreated(1);
        statisticGameRepository.save(statisticGameEntity);
        return new WebServiceResponse(true, "Statistic added to game successfully"); 
    }

    public List<Map<String, Object>> getStatisticByGame(Integer gameId) {
        List<Map<String, Object>> statistic = statisticRepository.getStatisticByGame(gameId);
        return statistic; 
    }

    
}
