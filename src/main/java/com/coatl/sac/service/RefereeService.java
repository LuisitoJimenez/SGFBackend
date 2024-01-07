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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.RefereeEntity;
import com.coatl.sac.entity.RefereeGenderEntity;
import com.coatl.sac.entity.UserRefereeEntity;
import com.coatl.sac.json.Birthplace;
import com.coatl.sac.model.RefereeDTO;
import com.coatl.sac.repository.RefereeGenderRepository;
import com.coatl.sac.repository.RefereeRepository;
import com.coatl.sac.repository.UserPlayerRepository;
import com.coatl.sac.repository.UserRefereeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.core.env.Environment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class RefereeService {

   @Autowired
   private RefereeRepository refereeRepository;

   @Autowired
   private RefereeGenderRepository refereeGenderRepository;

   @Autowired
   private UserRefereeRepository userRefereeRepository;

   @Autowired
   private UserPlayerRepository userPlayerRepository;

   private final Environment environment;

   private final S3Service objectStorage;

   private ObjectMapper objectMapper = new ObjectMapper();

   public List<Map<String, Object>> getRefereesList() {
      List<Map<String, Object>> allReferees = refereeRepository.getRefereesList();
      List<Map<String, Object>> modifiedReferess = new ArrayList<>();
      for (Map<String, Object> player : allReferees) {
         Map<String, Object> modifiedReferee = new HashMap<>(player);
         try {
            if (modifiedReferee.get("name") != null) {
               Map<String, Object> name = objectMapper.readValue(player.get("name").toString(), Map.class);
               modifiedReferee.put("name", name);
            }
            modifiedReferess.add(modifiedReferee);
         } catch (JsonProcessingException e) {
            log.error(e);
         }
      }
      return modifiedReferess;
   }

   @Transactional
   public Map<String, Object> createReferee(RefereeDTO refereeDto, Integer userId) {

      if (userRefereeRepository.findByUserId(userId).isPresent()) {
         throw new RuntimeException("User already has a referee");
      }

/*       if (userRefereeRepository.findById(userId).isPresent()) {
         throw new RuntimeException("User already has a player");
      } */

      RefereeEntity refereeEntity = new RefereeEntity();
      refereeEntity.setHeight(refereeDto.getHeight());
      refereeEntity.setWeight(refereeDto.getWeight());
      refereeEntity.setPhoto(refereeDto.getPhoto());
      refereeEntity.setIdentification(refereeDto.getIdentification());
      Date birthday = Date.valueOf(refereeDto.getBirthday());
      refereeEntity.setBirthday(birthday);
      refereeEntity.setCreatedBy(1);
      refereeEntity.setBirthplace(new Birthplace(refereeDto.getState(), refereeDto.getTown()));
      refereeRepository.save(refereeEntity);

      saveUserPlayer(userId, refereeEntity.getId());
      savePlayerGender(refereeEntity.getId(), refereeDto.getGender());

      return Map.of("id", refereeEntity.getId());
   }

   public RefereeGenderEntity savePlayerGender(Integer userId, Integer genderId) {
      RefereeGenderEntity refereeGenderEntity = new RefereeGenderEntity();
      refereeGenderEntity.setRefereeId(userId);
      refereeGenderEntity.setGenderId(genderId);
      refereeGenderEntity.setCreatedBy(1);
      refereeGenderRepository.save(refereeGenderEntity);

      return refereeGenderEntity;
   }

   public UserRefereeEntity saveUserPlayer(Integer userId, Integer refereeId) {
      UserRefereeEntity userRefereeEntity = new UserRefereeEntity();
      userRefereeEntity.setUserId(userId);
      userRefereeEntity.setRefereeId(refereeId);
      userRefereeEntity.setCreatedBy(1);
      userRefereeRepository.save(userRefereeEntity);

      return userRefereeEntity;
   }

   public Map<String, Object> getRefereeById(Integer refereeId) {
      Map<String, Object> referee = new HashMap<>(refereeRepository.getRefereeById(refereeId));
        try {
            if (referee.get("name") != null) {
                referee.put("name", objectMapper.readValue(referee.get("name").toString(), Map.class));
            }
        } catch (JsonProcessingException e) {
            log.error(e);
        }
        return referee;
   }

   @Transactional
   public WebServiceResponse deleteReferee(Integer refereeId) {
      RefereeEntity refereeEntity = refereeRepository.findById(refereeId)
            .orElseThrow(() -> new RuntimeException("Referee not found"));
      refereeEntity.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
      refereeRepository.save(refereeEntity);

      UserRefereeEntity userRefereeEntity = userRefereeRepository.findByRefereeId(refereeId)
            .orElseThrow(() -> new RuntimeException("Referee not found"));
      userRefereeEntity.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
      userRefereeRepository.save(userRefereeEntity);
      return new WebServiceResponse(true, "Referee deleted successfully");
   }

   @Transactional
   public WebServiceResponse updateReferee(RefereeDTO playerDto, Integer refereeId) {
      try {
         RefereeEntity refereeEntity = refereeRepository.findById(refereeId)
               .orElseThrow(() -> new RuntimeException("Referee not found"));
         refereeEntity.setHeight(playerDto.getHeight());
         refereeEntity.setWeight(playerDto.getWeight());
         Date birthday = Date.valueOf(playerDto.getBirthday());
         refereeEntity.setBirthday(birthday);
         refereeEntity.setBirthplace(new Birthplace(playerDto.getState(), playerDto.getTown()));
         refereeEntity.setCreatedBy(1);
         refereeRepository.save(refereeEntity);

         RefereeGenderEntity refereeGenderEntity = refereeGenderRepository.findByRefereeId(refereeId)
               .orElseThrow(() -> new RuntimeException("Referee not found"));
         refereeGenderEntity.setGenderId(playerDto.getGender());
         refereeGenderEntity.setCreatedBy(1);
         refereeGenderRepository.save(refereeGenderEntity);

         return new WebServiceResponse(true, "Referee updated successfully");

      } catch (Exception e) {

         return new WebServiceResponse(false, e.getMessage());
      }
   }

   @Transactional
   public WebServiceResponse saveImageReferee(Integer refereeId, MultipartFile file) {
      String bucketName = environment.getProperty("aws.s3.uploads-bucket");
      String objectkey = String.join("/", "referees", refereeId.toString(), file.getOriginalFilename());
      try {
         objectStorage.uploadFile(bucketName, objectkey, file.getInputStream());
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

         RefereeEntity refereeEntity = refereeRepository.findById(refereeId)
               .orElseThrow(() -> new RuntimeException("Player not found"));

         refereeEntity.setPhoto(filesAppend.toString());

         refereeRepository.save(refereeEntity);

      } catch (IOException e) {
         throw new RuntimeException("Error reading file");
      }

      return new WebServiceResponse(true, "Image saved successfully");
   }

   public byte[] getImageReferee(Integer refereeId, String fileId) {

      RefereeEntity refereeEntity = refereeRepository.findById(refereeId)
            .orElseThrow(() -> new RuntimeException("Player not found"));

      if (refereeEntity.getPhoto() == null) {
         throw new RuntimeException("Player has no photo");
      }

      Gson gson = new Gson();
      JsonArray photo = gson.fromJson(refereeEntity.getPhoto(), JsonArray.class);

      String bucketName = environment.getProperty("aws.s3.uploads-bucket");

      JsonObject photoObject = photo.get(0).getAsJsonObject();
      String photoKey = photoObject.get("key").getAsString();

      return objectStorage.getFile(photoKey, bucketName);
   }
}
