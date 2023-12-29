package com.coatl.sac.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.GameEntity;
import com.coatl.sac.entity.GameGenderEntity;
import com.coatl.sac.entity.GameRefereeEntity;
import com.coatl.sac.entity.GameSubEntity;
import com.coatl.sac.entity.GameTeamEntity;
import com.coatl.sac.entity.GenderEntity;
import com.coatl.sac.entity.RefereeEntity;
import com.coatl.sac.entity.SubEntity;
//import com.coatl.sac.json.UserName;
import com.coatl.sac.model.GameDTO;
import com.coatl.sac.repository.GameGenderRepository;
import com.coatl.sac.repository.GameRepository;
import com.coatl.sac.repository.GameTeamRepository;
import com.coatl.sac.repository.GenderRepository;
import com.coatl.sac.repository.RefereeRepository;
import com.coatl.sac.repository.SubRepository;
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

    @Autowired
    private GameGenderRepository gameGenderRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private SubRepository subsRepository;

    @Autowired
    private RefereeRepository refereeRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public WebServiceResponse createGame(GameDTO gameDto) {
        try {
        if (gameRepository.existsByGameDate(gameDto.getGameDate())) {
            return new WebServiceResponse(false, "There is a game with that date and time");
        } else if (gameRepository.existsByName(gameDto.getName())) {
            return new WebServiceResponse(false, "There is a game with that name");
        }
            GameEntity game = new GameEntity();
            game.setName(gameDto.getName());
            game.setField(gameDto.getField());
            game.setGameDate(gameDto.getGameDate());
            game.setUserCreated(1);
            gameRepository.save(game);

            Optional<GenderEntity> gender = genderRepository.findById(gameDto.getGenderId());            
            Optional<SubEntity> sub = subsRepository.findById(gameDto.getSubId());

            if (!gender.isPresent() || !sub.isPresent()) {
                throw new RuntimeException("Gender or Sub not found");
            }

            saveGameSub(game.getId(), gameDto.getSubId());

            saveGameGender(game.getId(), gameDto.getGenderId());

            Optional<RefereeEntity> referee = refereeRepository.findById(gameDto.getRefereeId());

            if (!referee.isPresent()) {
                throw new RuntimeException("Referee not found");
            }

            saveGameReferee(game.getId(), gameDto.getRefereeId());

            return new WebServiceResponse(true, "Game created");

        } catch (Exception e) {
            return new WebServiceResponse(false, e.getMessage());
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

    private GameGenderEntity saveGameGender(Integer gameId, Integer genderId) {
        GameGenderEntity gameGenderEntity = new GameGenderEntity();
        gameGenderEntity.setGameId(gameId);
        gameGenderEntity.setGenderId(genderId);
        gameGenderEntity.setUserCreated(1);
        gameGenderRepository.save(gameGenderEntity);
        return gameGenderEntity;
    }

    private GameSubEntity saveGameSub (Integer gameId, Integer subId) {
        GameSubEntity gameSubEntity = new GameSubEntity();
        gameSubEntity.setGameId(gameId);
        gameSubEntity.setSubId(subId);
        gameSubEntity.setUserCreated(1);
        return gameSubEntity;
    }

    private GameRefereeEntity saveGameReferee(Integer gameId, Integer refereeId) {
        GameRefereeEntity gameRefereeEntity = new GameRefereeEntity();
        gameRefereeEntity.setGameId(gameId);
        gameRefereeEntity.setRefereeId(refereeId);
        gameRefereeEntity.setUserCreated(1);
        return gameRefereeEntity;
    }

}
