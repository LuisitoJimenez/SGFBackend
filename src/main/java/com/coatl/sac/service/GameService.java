package com.coatl.sac.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
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
import com.coatl.sac.model.GameDTO;
import com.coatl.sac.repository.GameGenderRepository;
import com.coatl.sac.repository.GameRefereeRepository;
import com.coatl.sac.repository.GameRepository;
import com.coatl.sac.repository.GameSubRepository;
import com.coatl.sac.repository.GameTeamRepository;
import com.coatl.sac.repository.GenderRepository;
import com.coatl.sac.repository.RefereeRepository;
import com.coatl.sac.repository.SubRepository;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
//import lombok.extern.log4j.Log4j2;

@Service
// @Log4j2
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

    @Autowired
    private GameSubRepository gameSubRepository;

    @Autowired
    private GameRefereeRepository gameRefereeRepository;

    // private ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public WebServiceResponse createGame(GameDTO gameDTO) {
        try {
            if (gameRepository.existsByGameTimeAndDeletedIsNull(gameDTO.getGameTime())) {
                return new WebServiceResponse(false, "There is a game with that time");
            } else if (gameRepository.existsByNameAndDeletedIsNull(gameDTO.getName())) {
                return new WebServiceResponse(false, "There is a game with that name");
            }
            GameEntity game = new GameEntity();
            game.setName(gameDTO.getName());
            game.setField(gameDTO.getField());
            game.setGameTime(gameDTO.getGameTime());
            game.setGameDate(gameDTO.getGameDate());
            game.setUserCreated(1);
            gameRepository.save(game);

            Optional<GenderEntity> gender = genderRepository.findById(gameDTO.getGenderId());
            Optional<SubEntity> sub = subsRepository.findById(gameDTO.getSubId());

            if (!gender.isPresent() || !sub.isPresent()) {
                throw new RuntimeException("Gender or Sub not found");
            }

            saveGameSub(game.getId(), gameDTO.getSubId());

            saveGameGender(game.getId(), gameDTO.getGenderId());

            Optional<RefereeEntity> referee = refereeRepository.findById(gameDTO.getRefereeId());

            if (!referee.isPresent()) {
                throw new RuntimeException("Referee not found");
            }

            saveGameReferee(game.getId(), gameDTO.getRefereeId());

            return new WebServiceResponse(true, "Game created");

        } catch (Exception e) {
            return new WebServiceResponse(false, e.getMessage());
        }

    }

    @Transactional
    public WebServiceResponse assignGameTeam(Integer gameId, Integer teamId) {
        long countGameId = gameTeamRepository.countByGameIdAndDeletedIsNull(gameId);
        if (countGameId >= 2) {
            return new WebServiceResponse(false, "Two records already exist with the same gameId");
        }
        boolean exists = gameTeamRepository.existsByGameIdAndTeamIdAndDeletedIsNull(gameId, teamId);
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
        //List<Map<String, Object>> teams = gameRepository.getGameTeams(userId);
        //return teams;
        return gameRepository.getGameTeams(userId);
    }

    /*
     * public List<GameEntity> getGameList() {
     * List<GameEntity> games = gameRepository.findAll();
     * return games;
     * }
     */

    // @Transactional
    public List<Map<String, Object>> getGameList() {

        return gameRepository.getGameList();
        /*
         * List<Map<String, Object>> allGames = gameRepository.getGameList();
         * List<Map<String, Object>> modifiedGames = new ArrayList<>();
         * for (Map<String, Object> game : allGames) {
         * Map<String, Object> modifiedGame = new HashMap<>(game);
         * try {
         * if (modifiedGame.get("referee") != null) {
         * Map<String, Object> name =
         * objectMapper.readValue(game.get("referee").toString(), Map.class);
         * modifiedGame.put("referee", name);
         * }
         * modifiedGames.add(modifiedGame);
         * } catch (JsonProcessingException e) {
         * log.error(e);
         * }
         * }
         * return modifiedGames;
         */
    }

    private GameGenderEntity saveGameGender(Integer gameId, Integer genderId) {
        GameGenderEntity gameGenderEntity = new GameGenderEntity();
        gameGenderEntity.setGameId(gameId);
        gameGenderEntity.setGenderId(genderId);
        gameGenderEntity.setUserCreated(1);
        gameGenderRepository.save(gameGenderEntity);
        return gameGenderEntity;
    }

    private GameSubEntity saveGameSub(Integer gameId, Integer subId) {
        GameSubEntity gameSubEntity = new GameSubEntity();
        gameSubEntity.setGameId(gameId);
        gameSubEntity.setSubId(subId);
        gameSubEntity.setUserCreated(1);
        gameSubRepository.save(gameSubEntity);
        return gameSubEntity;
    }

    private GameRefereeEntity saveGameReferee(Integer gameId, Integer refereeId) {
        GameRefereeEntity gameRefereeEntity = new GameRefereeEntity();
        gameRefereeEntity.setGameId(gameId);
        gameRefereeEntity.setRefereeId(refereeId);
        gameRefereeEntity.setUserCreated(1);
        gameRefereeRepository.save(gameRefereeEntity);
        return gameRefereeEntity;
    }

    @Transactional
    public WebServiceResponse deleteGame(Integer gameId) {
        try {

            GameEntity game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
            game.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
            ;
            gameRepository.save(game);

            GameSubEntity gameSub = gameSubRepository.findByGameId(gameId)
                    .orElseThrow(() -> new RuntimeException("GameSub not found"));
            gameSub.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
            gameSubRepository.save(gameSub);

            GameGenderEntity gameGender = gameGenderRepository.findByGameId(gameId)
                    .orElseThrow(() -> new RuntimeException("GameGender not found"));
            gameGender.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
            gameGenderRepository.save(gameGender);

            GameRefereeEntity gameReferee = gameRefereeRepository.findByGameId(gameId)
                    .orElseThrow(() -> new RuntimeException("GameReferee not found"));
            gameReferee.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
            gameRefereeRepository.save(gameReferee);

            return new WebServiceResponse(false, "Game deleted");
        } catch (Exception e) {
            return new WebServiceResponse(false, e.getMessage());
        }
    }

    @Transactional
    public WebServiceResponse updateGame(GameDTO gameDTO, Integer gameId) {
        try {

            GameEntity game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
            game.setName(gameDTO.getName());
            game.setField(gameDTO.getField());
            game.setGameDate(gameDTO.getGameDate());
            game.setGameTime(gameDTO.getGameTime());
            gameRepository.save(game);

            GameGenderEntity gameGender = gameGenderRepository.findByGameIdAndDeletedIsNull(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            gameGender.setGenderId(gameDTO.getGenderId());
            gameGender.setUserCreated(1);
            gameGenderRepository.save(gameGender);

            GameSubEntity gameSub = gameSubRepository.findByGameIdAndDeletedIsNull(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            gameSub.setSubId(gameDTO.getSubId());
            gameSub.setUserCreated(1);
            gameSubRepository.save(gameSub);

            GameRefereeEntity gameReferee = gameRefereeRepository.findByGameIdAndDeletedIsNull(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            gameReferee.setRefereeId(gameDTO.getRefereeId());
            gameReferee.setUserCreated(1);
            gameRefereeRepository.save(gameReferee);

            return new WebServiceResponse(true, "Game updated");
        } catch (Exception e) {

            return new WebServiceResponse(false, e.getMessage());
        }
    }

    public WebServiceResponse deleteGameTeam(Integer gameId, Integer teamId) {
        try {
            GameTeamEntity gameTeam = gameTeamRepository.findByGameIdAndTeamIdAndDeletedIsNull(gameId, teamId)
                    .orElseThrow(() -> new RuntimeException("GameTeam not found"));
            gameTeam.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
            gameTeamRepository.save(gameTeam);
            return new WebServiceResponse(true, "GameTeam deleted");
        } catch (Exception e) {
            return new WebServiceResponse(false, e.getMessage());
        }
    }


    public WebServiceResponse getGameById(Integer gameId) {
        try {
            Map<String, Object> game = gameRepository.findGameById(gameId);
            return new WebServiceResponse(game);
        } catch (Exception e) {
            return new WebServiceResponse(false, e.getMessage());
        }
    }
}
