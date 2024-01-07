package com.coatl.sac.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.FieldEntity;
import com.coatl.sac.entity.FieldGameEntity;
import com.coatl.sac.entity.FieldEntity;
import com.coatl.sac.json.Address;
import com.coatl.sac.model.FieldDTO;
import com.coatl.sac.repository.FieldGameRepository;
import com.coatl.sac.repository.FieldRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final Environment environment;
    private final FieldRepository fieldRepository;
    private final S3Service objectStorage;
    private final MessageSource messageSource;
    private final FieldGameRepository fieldGameRepository;

    @Transactional
    public WebServiceResponse createField(FieldDTO fieldDTO) {

        Locale locale = LocaleContextHolder.getLocale();

        if (fieldRepository.findByNameAndDeletedAtIsNull(fieldDTO.getName()).isPresent()) {
            return new WebServiceResponse(false, messageSource.getMessage("field.same.name", null, locale));
        }

        FieldEntity field = new FieldEntity();
        field.setName(fieldDTO.getName());
        field.setPhone(fieldDTO.getPhone());
        field.setEmail(fieldDTO.getEmail());
        field.setAddress(new Address(fieldDTO.getStreet(), fieldDTO.getPostalCode(), fieldDTO.getMunicipality(),
                fieldDTO.getTown(), fieldDTO.getState()));
        field.setCapacity(fieldDTO.getCapacity());
        field.setCreatedBy(1);
        fieldRepository.save(field);

        return new WebServiceResponse(true, "Field created successfully", field.getId());
    }

    public List<Map<String, Object>> getFieldsList() {
        return fieldRepository.getFieldsList();
    }

    @Transactional
    public WebServiceResponse deleteField(Integer fieldId) {
        Locale locale = LocaleContextHolder.getLocale();

        FieldEntity field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new RuntimeException("Field not found"));
        field.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        // field.setDeletedBy(1);
        fieldRepository.save(field);
        return new WebServiceResponse(true, messageSource.getMessage("field.delete.success", null, locale));
    }

    @Transactional
    public WebServiceResponse updateField(FieldDTO fieldDTO, Integer fieldId) {
        Locale locale = LocaleContextHolder.getLocale();

        try {
            FieldEntity field = fieldRepository.findById(fieldId)
                    .orElseThrow(() -> new RuntimeException("Field not found"));
            field.setName(fieldDTO.getName());
            field.setPhone(fieldDTO.getPhone());
            field.setEmail(fieldDTO.getEmail());
            field.setAddress(new Address(fieldDTO.getStreet(), fieldDTO.getPostalCode(), fieldDTO.getMunicipality(),
                    fieldDTO.getTown(), fieldDTO.getState()));
            field.setCapacity(fieldDTO.getCapacity());
            fieldRepository.save(field);
            
/*             FieldGameEntity fieldGame = fieldGameRepository.findByFieldId(fieldId)
                    .orElseThrow(() -> new RuntimeException("Field not found")); */

            return new WebServiceResponse(true, messageSource.getMessage("field.update.success", null, locale));

        } catch (Exception e) {

            return new WebServiceResponse(false, messageSource.getMessage("field.update.error", null, locale));
        }
    }

    @Transactional
    public WebServiceResponse saveImageField(Integer fieldId, MultipartFile file) {
        String bucketName = environment.getProperty("aws.s3.uploads-bucket");
        String objectKey = String.join("/", "fields", fieldId.toString(), file.getOriginalFilename());

        try {
            objectStorage.uploadFile(bucketName, objectKey, file.getInputStream());

            JsonObject fileData = new JsonObject();

            fileData.addProperty("key", objectKey);
            fileData.addProperty("name", file.getOriginalFilename());
            fileData.addProperty("sizeBytes", file.getBytes().length);
            fileData.addProperty("type", Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            fileData.addProperty("tsCreated", Instant.now().getEpochSecond());

            JsonArray logo = new JsonArray();
            logo.add(fileData);

            FieldEntity fieldEntity = fieldRepository.findById(fieldId)
                    .orElseThrow(() -> new RuntimeException("Field not found"));
            fieldEntity.setLogo(logo.toString());
            fieldRepository.save(fieldEntity);

        } catch (IOException e) {
            throw new RuntimeException("Error uploading file");
        }

        return new WebServiceResponse(true, "Image saved successfully");
    }

    public byte[] getImageField(Integer fieldId, String fileId) {
        FieldEntity fieldEntity = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        if (fieldEntity.getLogo() == null) {
            throw new RuntimeException("Field has no image");
        }

        Gson gson = new Gson();
        JsonArray logo = gson.fromJson(fieldEntity.getLogo(), JsonArray.class);

        String bucketName = environment.getProperty("aws.s3.uploads-bucket");

        JsonObject logoObject = logo.get(0).getAsJsonObject();
        String logoKey = logoObject.get("key").getAsString();

        return objectStorage.getFile(logoKey, bucketName);
    }

    public Map<String, Object> getFieldById(Integer fieldId) {
        fieldRepository.findById(fieldId).orElseThrow(() -> new RuntimeException("Club not found"));
        Map<String, Object> club = new HashMap<>(fieldRepository.getFieldById(fieldId));
        return club;
    }

}
