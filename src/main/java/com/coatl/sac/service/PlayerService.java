package com.coatl.sac.service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
//import com.google.gson.ToNumberPolicy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.PlayerEntity;
import com.coatl.sac.entity.PlayerGenderEntity;
import com.coatl.sac.entity.UserPlayerEntity;
import com.coatl.sac.json.Birthplace;
import com.coatl.sac.model.PlayerDTO;
import com.coatl.sac.repository.PlayerGenderRepository;
import com.coatl.sac.repository.PlayerRepository;
import com.coatl.sac.repository.UserPlayerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
/* import software.amazon.awssdk.services.cognitoidentityprovider.endpoints.internal.Value.Str;
import com.google.gson.Gson;
import com.google.gson.JsonArray; */

@Log4j2
@Service
@RequiredArgsConstructor
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerGenderRepository playerGenderRepository;

    @Autowired
    private UserPlayerRepository userPlayerRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final S3Service objectStorage;

    private final Environment environment;

    /*
     * private final Gson gson = new GsonBuilder()
     * .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
     * .create();
     */

    public List<Map<String, Object>> getPlayerList() {
        List<Map<String, Object>> allPlayers = playerRepository.getListPlayers();
        List<Map<String, Object>> modifiedPlayers = new ArrayList<>();
        for (Map<String, Object> player : allPlayers) {
            Map<String, Object> modifiedPlayer = new HashMap<>(player);
            try {
                if (modifiedPlayer.get("name") != null) {
                    Map<String, Object> name = objectMapper.readValue(player.get("name").toString(), Map.class);
                    modifiedPlayer.put("name", name);
                }
                modifiedPlayers.add(modifiedPlayer);
            } catch (JsonProcessingException e) {
                log.error(e);
            }
        }
        return modifiedPlayers;
    }

    public Map<String, Object> getPlayerById(Integer playerId) {
        Map<String, Object> player = new HashMap<>(playerRepository.getPlayerById(playerId));
        try {
            if (player.get("name") != null) {
                player.put("name", objectMapper.readValue(player.get("name").toString(), Map.class));
            }
        } catch (JsonProcessingException e) {
            log.error(e);
        }
        return player;
    }

    @Transactional
    public Map<String, Object> createPlayer(PlayerDTO playerDto, Integer userId) {

        if (userPlayerRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("User already has a player");
        }

        PlayerEntity playerEntity = new PlayerEntity();

        playerEntity.setHeight(playerDto.getHeight());
        playerEntity.setWeight(playerDto.getWeight());
        playerEntity.setPhoto(playerDto.getPhoto());
        playerEntity.setIdentification(playerDto.getIdentification());
        Date birthday = Date.valueOf(playerDto.getBirthday());
        playerEntity.setBirthday(birthday);
        playerEntity.setUserCreated(1);
        playerEntity.setBirthplace(new Birthplace(playerDto.getState(), playerDto.getTown()));

        playerRepository.save(playerEntity);

        saveUserPlayer(userId, playerEntity.getId());
        savePlayerGender(playerEntity.getId(), playerDto.getGender());

        // return new WebServiceResponse(true, "Player created", playerEntity.getId());
        return Map.of("id", playerEntity.getId());
    }

    private PlayerGenderEntity savePlayerGender(Integer userId, Integer genderId) {
        PlayerGenderEntity playerGenderEntity = new PlayerGenderEntity();
        playerGenderEntity.setPlayerId(userId);
        playerGenderEntity.setGenderId(genderId);
        playerGenderEntity.setUserCreated(1);
        playerGenderRepository.save(playerGenderEntity);

        return playerGenderEntity;
    }

    private UserPlayerEntity saveUserPlayer(Integer userId, Integer playerId) {
        UserPlayerEntity userPlayerEntity = new UserPlayerEntity();
        userPlayerEntity.setUserId(userId);
        userPlayerEntity.setPlayerId(playerId);
        userPlayerEntity.setUserCreated(1);
        userPlayerRepository.save(userPlayerEntity);

        return userPlayerEntity;
    }

    @Transactional
    public WebServiceResponse deletePlayer(Integer playerId) {
        PlayerEntity playerEntity = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        playerEntity.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
        playerRepository.save(playerEntity);

        /*
         * PlayerGenderEntity playerGenderEntity =
         * playerGenderRepository.findByPlayerId(playerId)
         * .orElseThrow(() -> new RuntimeException("Player not found"));
         * playerGenderEntity.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
         * playerGenderRepository.save(playerGenderEntity);
         */

        UserPlayerEntity userPlayerEntity = userPlayerRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        userPlayerEntity.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
        userPlayerRepository.save(userPlayerEntity);
        return new WebServiceResponse(true, "Player deleted successfully");
    }

    @Transactional
    public WebServiceResponse updatePlayer(PlayerDTO playerDto, Integer playerId) {
        try {
            PlayerEntity playerEntity = playerRepository.findById(playerId)
                    .orElseThrow(() -> new RuntimeException("Player not found"));
            // playerEntity.setIdentification(playerDto.getIdentification());
            playerEntity.setHeight(playerDto.getHeight());
            playerEntity.setWeight(playerDto.getWeight());
            Date birthday = Date.valueOf(playerDto.getBirthday());
            playerEntity.setBirthday(birthday);
            playerEntity.setBirthplace(new Birthplace(playerDto.getState(), playerDto.getTown()));
            playerEntity.setUserCreated(1);
            playerRepository.save(playerEntity);

            PlayerGenderEntity playerGenderEntity = playerGenderRepository.findByPlayerId(playerId)
                    .orElseThrow(() -> new RuntimeException("Player not found"));
            playerGenderEntity.setGenderId(playerDto.getGender());
            playerGenderEntity.setUserCreated(1);
            playerGenderRepository.save(playerGenderEntity);

            return new WebServiceResponse(true, "Player updated successfully");

        } catch (Exception e) {

            return new WebServiceResponse(false, e.getMessage());
        }
    }

    @Transactional
    public WebServiceResponse saveImagePlayer(Integer playerId, MultipartFile file) {
        String bucketName = environment.getProperty("aws.s3.uploads-bucket");
        String objectkey = String.join("/", "players", playerId.toString(), file.getOriginalFilename());
        try {
            objectStorage.uploadFile(bucketName, objectkey, file.getInputStream());
            /*
             * Map<String, Object> fileData = new HashMap<>();
             * fileData.put("key", objectkey);
             * fileData.put("sizeBytes", file.getBytes().length);
             * fileData.put("type", Objects.requireNonNull(file.getOriginalFilename())
             * .substring(file.getOriginalFilename().lastIndexOf(".") + 1));
             * fileData.put("tsCreated", Instant.now().getEpochSecond());
             * 
             * String url = "https://" + bucketName + ".s3.amazonaws.com/" + objectkey;
             */

            JsonObject fileData = new JsonObject();
            fileData.addProperty("key", objectkey);
            fileData.addProperty("name", file.getOriginalFilename());
            fileData.addProperty("sizeBytes", file.getBytes().length);
            fileData.addProperty("type",
                    Objects.requireNonNull(file.getOriginalFilename())
                            .substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            fileData.addProperty("tsCreated", Instant.now().getEpochSecond());

            JsonArray filesAppend = new JsonArray();
            filesAppend.add(fileData);
            // Map<String, Object> filesMap = gson.fromJson(filesAppend, Map.class);

            PlayerEntity playerEntity = playerRepository.findById(playerId)
                    .orElseThrow(() -> new RuntimeException("Player not found"));

            playerEntity.setPhoto(filesAppend.toString());

            // playerEntity.setPhoto(objectkey);
            // playerEntity.setPhoto(url);
            playerRepository.save(playerEntity);

        } catch (IOException e) {
            throw new RuntimeException("Error reading file");
        }

        return new WebServiceResponse(true, "Image saved successfully");
    }

    public byte[] getImagePlayer(Integer playerId, String fileId) {

        PlayerEntity playerEntity = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        if (playerEntity.getPhoto() == null) {
            throw new RuntimeException("Player has no photo");
        }

        Gson gson = new Gson();
        JsonArray photo = gson.fromJson(playerEntity.getPhoto(), JsonArray.class);

        String bucketName = environment.getProperty("aws.s3.uploads-bucket");

        JsonObject photoObject = photo.get(0).getAsJsonObject();
        String photoKey = photoObject.get("key").getAsString();

        return objectStorage.getFile(photoKey, bucketName);
    }

}
