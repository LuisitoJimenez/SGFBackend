package com.coatl.sac.service;

import java.util.Locale;

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
}
