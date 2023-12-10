package com.coatl.sac.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.GameEntity;
import com.coatl.sac.entity.GameTeamEntity;
import com.coatl.sac.json.UserName;
import com.coatl.sac.model.GameDTO;
import com.coatl.sac.repository.GameRepository;
import com.coatl.sac.repository.GameTeamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameTeamRepository gameTeamRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public WebServiceResponse createGame(GameDTO gameDto) {
        if (gameRepository.existsByGameDateTime(gameDto.getGameDateTime())) {
            return new WebServiceResponse(false, "There is a game with that date and time");
        } else if (gameRepository.existsByName(gameDto.getName())) {
            return new WebServiceResponse(false, "There is a game with that name");
        } else {
            GameEntity gameEntity = new GameEntity();
            gameEntity.setName(gameDto.getName());
            gameEntity.setReferee(new UserName(gameDto.getReferee().getFirst(), gameDto.getReferee().getLast(), gameDto.getReferee().getSecondLast()));
            gameEntity.setGameDateTime(Timestamp.valueOf(gameDto.getGameDateTime()));
            gameEntity.setUserCreated(gameDto.getUserCreated());
            gameRepository.save(gameEntity);
            return new WebServiceResponse(true, "Game created");
        }
    }

    @Transactional
    public WebServiceResponse assignGameTeam(Integer gameId, Integer teamId) {
        long countGameId = gameTeamRepository.countByGameId(gameId);
        if (countGameId >= 2) {
            return new WebServiceResponse(false, "Two records already exist with the same gameId");
        }
        boolean exists = gameTeamRepository.existsByGameIdAndTeamId(gameId, teamId);
        if (exists) {
            return new WebServiceResponse(false, "A record already exists with the same gameId and teamId");
        }

        GameTeamEntity gameTeamEntity = new GameTeamEntity();
        gameTeamEntity.setGameId(gameId);
        gameTeamEntity.setTeamId(teamId);
        gameTeamEntity.setUserCreated(1);
        gameTeamRepository.save(gameTeamEntity);
        return new WebServiceResponse(true, "Game team assigned");
    }

    public List<Map<String, Object>> getGameTeams(Integer userId) {
        List<Map<String, Object>> teams = gameRepository.getGameTeams(userId);
        return teams;
    }

    /*
     * public List<GameEntity> getGameList() {
     * List<GameEntity> games = gameRepository.findAll();
     * return games;
     * }
     */

    @Transactional
    public List<Map<String, Object>> getGameList() {
        List<Map<String, Object>> allGames = gameRepository.getGameList();
        List<Map<String, Object>> modifiedGames = new ArrayList<>();
        for (Map<String, Object> game : allGames) {
            Map<String, Object> modifiedGame = new HashMap<>(game);
            try {
                if (modifiedGame.get("referee") != null) {
                    Map<String, Object> name = objectMapper.readValue(game.get("referee").toString(), Map.class);
                    modifiedGame.put("referee", name);
                }
                modifiedGames.add(modifiedGame);
            } catch (JsonProcessingException e) {
                log.error(e);
            }
        }
        return modifiedGames;
    }
}
