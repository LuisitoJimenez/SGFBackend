package com.coatl.sac.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.FieldEntity;
import com.coatl.sac.json.Address;
import com.coatl.sac.model.FieldDTO;
import com.coatl.sac.repository.FieldRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final Environment environment;
    private final FieldRepository fieldRepository;
    private final S3Service s3Service;
    private final MessageSource messageSource;

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

        return new WebServiceResponse(true, "Field created successfully");
    }

    public List<Map<String, Object>> getFieldsList() {
        return fieldRepository.getFieldsList();
    }

    @Transactional
    public WebServiceResponse deleteField(Integer fieldId) {
        Locale locale = LocaleContextHolder.getLocale();

        FieldEntity field = fieldRepository.findById(fieldId).orElseThrow(() -> new RuntimeException("Field not found"));
        field.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        field.setDeletedBy(1);
        fieldRepository.save(field);
        return new WebServiceResponse(true, messageSource.getMessage("field.delete.success", null, locale));
    }

    @Transactional
    public WebServiceResponse updateField(FieldDTO fieldDTO, Integer fieldId) {
        Locale locale = LocaleContextHolder.getLocale();

        try {
            FieldEntity field = fieldRepository.findById(fieldId).orElseThrow(() -> new RuntimeException("Field not found"));
            field.setName(fieldDTO.getName());
            field.setPhone(fieldDTO.getPhone());
            field.setEmail(fieldDTO.getEmail());
            field.setAddress(new Address(fieldDTO.getStreet(), fieldDTO.getPostalCode(), fieldDTO.getMunicipality(),
                fieldDTO.getTown(), fieldDTO.getState()));
            field.setCapacity(fieldDTO.getCapacity());
            fieldRepository.save(field);

            return new WebServiceResponse(true, messageSource.getMessage("field.update.success", null, locale));

        } catch (Exception e) {
            
            return new WebServiceResponse(false, messageSource.getMessage("field.update.error", null, locale));
        }
    }

}
