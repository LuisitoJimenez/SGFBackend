package com.coatl.sac.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.ClubEntity;
import com.coatl.sac.json.SocialNetwork;
import com.coatl.sac.model.ClubDTO;
import com.coatl.sac.repository.ClubRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    private final Environment environment;

    private final S3Service objectStorage;

    public List<Map<String, Object>> getClubsList() {
        List<Map<String, Object>> allClubs = clubRepository.getClubsList();
        return allClubs;
    }

    @Transactional
    public Map<String, Object> createClub(ClubDTO clubDto) {

        if (clubRepository.existsDeletedClubByName(clubDto.getName()) == 1) {
            throw new RuntimeException("Club already exists");
        }

        ClubEntity clubEntity = new ClubEntity();
        clubEntity.setName(clubDto.getName());
        clubEntity.setLogo(clubDto.getLogo());
        clubEntity.setPhone(clubDto.getPhone());
        clubEntity.setEmail(clubDto.getEmail());
        clubEntity.setSocialNetworks(new SocialNetwork(clubDto.getFacebook(), clubDto.getTwitter(), clubDto.getTiktok(),
                clubDto.getInstagram(), clubDto.getYoutube(), clubDto.getOther(), clubDto.getWebsite()));
        clubEntity.setCreatedBy(1);
        clubRepository.save(clubEntity);

        return Map.of("id", clubEntity.getId());
        //return new WebServiceResponse(true, "Club created successfully");
    }

    @Transactional
    public WebServiceResponse updateClub(ClubDTO clubDto, Integer clubId) {
        try {

            ClubEntity clubEntity = clubRepository.findById(clubId)
                    .orElseThrow(() -> new RuntimeException("Club not found"));
            clubEntity.setName(clubDto.getName());
            clubEntity.setLogo(clubDto.getLogo());
            clubEntity.setPhone(clubDto.getPhone());
            clubEntity.setEmail(clubDto.getEmail());
            clubEntity.setSocialNetworks(
                    new SocialNetwork(clubDto.getFacebook(), clubDto.getTwitter(), clubDto.getTiktok(),
                            clubDto.getInstagram(), clubDto.getYoutube(), clubDto.getOther(), clubDto.getWebsite()));
            clubEntity.setCreatedBy(1);
            clubRepository.save(clubEntity);

            return new WebServiceResponse(true, "Club updated successfully");

        } catch (Exception e) {
            return new WebServiceResponse(e.getMessage());
        }
    }

    public Map<String, Object> getClubById(Integer clubId) {
        clubRepository.findById(clubId).orElseThrow(() -> new RuntimeException("Club not found"));
        Map<String, Object> club = new HashMap<>(clubRepository.getClubById(clubId));
        return club;
    }

    @Transactional
    public WebServiceResponse deleteClub(Integer clubId) {
        ClubEntity clubEntity = clubRepository.findById(clubId).orElseThrow(() -> new RuntimeException("Club not found"));
        clubEntity.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        clubRepository.save(clubEntity);
        
        return new WebServiceResponse(true, "Club deleted successfully");

    }

    @Transactional
    public WebServiceResponse saveImageClub(Integer clubId, MultipartFile file) {
        String bucketName = environment.getProperty("aws.s3.uploads-bucket");
        String objectKey = String.join("/", "clubs", clubId.toString(), file.getOriginalFilename());
        
        try {
            objectStorage.uploadFile(bucketName, objectKey, file.getInputStream());
            
            JsonObject fileData = new JsonObject();
            
            fileData.addProperty("key", objectKey);
            fileData.addProperty("name", file.getOriginalFilename());
            fileData.addProperty("sizeBytes", file.getBytes().length);
            fileData.addProperty("type", Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            fileData.addProperty("tsCreated", Instant.now().getEpochSecond());

            JsonArray logo = new JsonArray();
            logo.add(fileData);

            ClubEntity clubEntity = clubRepository.findById(clubId).orElseThrow(() -> new RuntimeException("Club not found"));
            clubEntity.setLogo(logo.toString());
            clubRepository.save(clubEntity);

        } catch (IOException e) {
            throw new RuntimeException("Error uploading file");
        }

        return new WebServiceResponse(true, "Image saved successfully");
    }

    public byte[] getImageClub(Integer clubId, String fileId) {
        ClubEntity clubEntity = clubRepository.findById(clubId).orElseThrow(() -> new RuntimeException("Club not found"));

        if (clubEntity.getLogo() == null) {
            throw new RuntimeException("Club has no image");
        }

        Gson gson = new Gson();
        JsonArray logo = gson.fromJson(clubEntity.getLogo(), JsonArray.class);

        String bucketName = environment.getProperty("aws.s3.uploads-bucket");

        JsonObject logoObject = logo.get(0).getAsJsonObject();
        String logoKey = logoObject.get("key").getAsString();

        return objectStorage.getFile(logoKey, bucketName);
    }
}
