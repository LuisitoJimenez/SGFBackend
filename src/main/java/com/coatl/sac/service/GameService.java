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
import com.coatl.sac.entity.FieldEntity;
import com.coatl.sac.entity.FieldGameEntity;
import com.coatl.sac.entity.GameEntity;
import com.coatl.sac.entity.GameGenderEntity;
import com.coatl.sac.entity.GameRefereeEntity;
import com.coatl.sac.entity.GameSubEntity;
import com.coatl.sac.entity.GameTeamEntity;
import com.coatl.sac.entity.GenderEntity;
import com.coatl.sac.entity.RefereeEntity;
import com.coatl.sac.entity.SubEntity;
import com.coatl.sac.model.GameDTO;
import com.coatl.sac.repository.FieldGameRepository;
import com.coatl.sac.repository.FieldRepository;
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
import com.ctc.wstx.shaded.msv_core.grammar.xmlschema.Field;

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

    @Autowired
    private FieldGameRepository fieldGameRepository;

    @Autowired
    private FieldRepository fieldRepository;

    // private ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public WebServiceResponse createGame(GameDTO gameDTO) {
        try {
            if (gameRepository.existsByGameTimeAndDeletedAtIsNull(gameDTO.getGameTime())) {
                return new WebServiceResponse(false, "There is a game with that time");
            } else if (gameRepository.existsByNameAndDeletedAtIsNull(gameDTO.getName())) {
                return new WebServiceResponse(false, "There is a game with that name");
            }
            GameEntity game = new GameEntity();
            game.setName(gameDTO.getName());
            //game.setField(gameDTO.getField());
            game.setGameTime(gameDTO.getGameTime());
            game.setGameDate(gameDTO.getGameDate());
            game.setCreatedBy(1);
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

            Optional<FieldEntity> fieldGame = fieldRepository.findById(gameDTO.getFieldId());

            if (!fieldGame.isPresent()) {
                throw new RuntimeException("Field not found");
            }

            saveFieldGame(game.getId(), gameDTO.getFieldId());

            return new WebServiceResponse(true, "Game created");

        } catch (Exception e) {
            return new WebServiceResponse(false, e.getMessage());
        }

    }

    @Transactional
    public WebServiceResponse assignGameTeam(Integer gameId, Integer teamId) {
        long countGameId = gameTeamRepository.countByGameIdAndDeletedAtIsNull(gameId);
        if (countGameId >= 2) {
            return new WebServiceResponse(false, "Two records already exist with the same gameId");
        }
        boolean exists = gameTeamRepository.existsByGameIdAndTeamIdAndDeletedAtIsNull(gameId, teamId);
        if (exists) {
            return new WebServiceResponse(false, "A record already exists with the same gameId and teamId");
        }

        GameTeamEntity gameTeamEntity = new GameTeamEntity();
        gameTeamEntity.setGameId(gameId);
        gameTeamEntity.setTeamId(teamId);
        gameTeamEntity.setCreatedBy(1);
        gameTeamRepository.save(gameTeamEntity);
        return new WebServiceResponse(true, "Game team assigned");
    }

    public List<Map<String, Object>> getGameTeams(Integer userId) {
        return gameRepository.getGameTeams(userId);
    }

    public List<Map<String, Object>> getGameList() {

        return gameRepository.getGameList();
    }

    private GameGenderEntity saveGameGender(Integer gameId, Integer genderId) {
        GameGenderEntity gameGenderEntity = new GameGenderEntity();
        gameGenderEntity.setGameId(gameId);
        gameGenderEntity.setGenderId(genderId);
        gameGenderEntity.setCreatedBy(1);
        gameGenderRepository.save(gameGenderEntity);
        return gameGenderEntity;
    }

    private GameSubEntity saveGameSub(Integer gameId, Integer subId) {
        GameSubEntity gameSubEntity = new GameSubEntity();
        gameSubEntity.setGameId(gameId);
        gameSubEntity.setSubId(subId);
        gameSubEntity.setCreatedBy(1);
        gameSubRepository.save(gameSubEntity);
        return gameSubEntity;
    }

    private GameRefereeEntity saveGameReferee(Integer gameId, Integer refereeId) {
        GameRefereeEntity gameRefereeEntity = new GameRefereeEntity();
        gameRefereeEntity.setGameId(gameId);
        gameRefereeEntity.setRefereeId(refereeId);
        gameRefereeEntity.setCreatedBy(1);
        gameRefereeRepository.save(gameRefereeEntity);
        return gameRefereeEntity;
    }

    private FieldGameEntity saveFieldGame(Integer gameId, Integer fieldId) {
        FieldGameEntity fieldGameEntity = new FieldGameEntity();
        fieldGameEntity.setGameId(gameId);
        fieldGameEntity.setFieldId(fieldId);
        fieldGameEntity.setCreatedBy(1);
        fieldGameRepository.save(fieldGameEntity);
        return fieldGameEntity;
    }

    @Transactional
    public WebServiceResponse deleteGame(Integer gameId) {
        try {

            GameEntity game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
            game.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            ;
            gameRepository.save(game);

            GameSubEntity gameSub = gameSubRepository.findByGameId(gameId)
                    .orElseThrow(() -> new RuntimeException("GameSub not found"));
            gameSub.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            gameSubRepository.save(gameSub);

            GameGenderEntity gameGender = gameGenderRepository.findByGameId(gameId)
                    .orElseThrow(() -> new RuntimeException("GameGender not found"));
            gameGender.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            gameGenderRepository.save(gameGender);

            GameRefereeEntity gameReferee = gameRefereeRepository.findByGameId(gameId)
                    .orElseThrow(() -> new RuntimeException("GameReferee not found"));
            gameReferee.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            gameRefereeRepository.save(gameReferee);

            FieldGameEntity fieldGame = fieldGameRepository.findByGameId(gameId)
                    .orElseThrow(() -> new RuntimeException("FieldGame not found"));
            fieldGame.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            fieldGameRepository.save(fieldGame);

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
            //game.setField(gameDTO.getField());
            game.setGameDate(gameDTO.getGameDate());
            game.setGameTime(gameDTO.getGameTime());
            gameRepository.save(game);

            GameGenderEntity gameGender = gameGenderRepository.findByGameIdAndDeletedAtIsNull(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            gameGender.setGenderId(gameDTO.getGenderId());
            gameGender.setCreatedBy(1);
            gameGenderRepository.save(gameGender);

            GameSubEntity gameSub = gameSubRepository.findByGameIdAndDeletedAtIsNull(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            gameSub.setSubId(gameDTO.getSubId());
            gameSub.setCreatedBy(1);
            gameSubRepository.save(gameSub);

            GameRefereeEntity gameReferee = gameRefereeRepository.findByGameIdAndDeletedAtIsNull(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            gameReferee.setRefereeId(gameDTO.getRefereeId());
            gameReferee.setCreatedBy(1);
            gameRefereeRepository.save(gameReferee);

            FieldGameEntity fieldGame = fieldGameRepository.findByGameIdAndDeletedAtIsNull(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            fieldGame.setFieldId(gameDTO.getFieldId());
            fieldGame.setCreatedBy(1);
            fieldGameRepository.save(fieldGame);

            return new WebServiceResponse(true, "Game updated");
        } catch (Exception e) {

            return new WebServiceResponse(false, e.getMessage());
        }
    }

    public WebServiceResponse deleteGameTeam(Integer gameId, Integer teamId) {
        try {
            GameTeamEntity gameTeam = gameTeamRepository.findByGameIdAndTeamIdAndDeletedAtIsNull(gameId, teamId)
                    .orElseThrow(() -> new RuntimeException("GameTeam not found"));
            gameTeam.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
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
