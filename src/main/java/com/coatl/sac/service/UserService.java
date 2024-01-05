package com.coatl.sac.service;

import org.springframework.stereotype.Service;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.UserEntity;
import com.coatl.sac.json.UserName;
import com.coatl.sac.model.UserDTO;
import com.coatl.sac.repository.UserRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserResult;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesResult;
import com.amazonaws.services.cognitoidp.model.AdminUserGlobalSignOutResult;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final CognitoService cognitoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> getUserList() {
        List<Map<String, Object>> allUsers = userRepository.getListOfUsers();
        List<Map<String, Object>> modifiedUsers = new ArrayList<>();
        for (Map<String, Object> user : allUsers) {
            Map<String, Object> modifiedUser = new HashMap<>(user);
            try {
                if (modifiedUser.get("name") != null) {
                    Map<String, Object> name = objectMapper.readValue(user.get("name").toString(), Map.class);
                    modifiedUser.put("name", name);
                }
                modifiedUsers.add(modifiedUser);
            } catch (JsonProcessingException e) {
                log.error(e);
            }
        }

        return modifiedUsers;
    }

    public Map<String, Object> getUserById(Integer userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Map<String, Object> newMap = new HashMap<>(userRepository.getUserById(userId));
        try {
            if (newMap.get("name") != null) {
                newMap.put("name", objectMapper.readValue(newMap.get("name").toString(), Map.class));
            }
        } catch (JsonProcessingException e) {
            log.error(e);
        }
        return newMap;
    }

    @Transactional
    public WebServiceResponse createUser(UserDTO userDto) {
        System.out.println("UserDTO: " + userDto);
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return new WebServiceResponse(false, "Email already registered");
        }

        AdminCreateUserResult createUserResult = cognitoService.createUser(userDto.getEmail(), userDto.getPassword(),
                userDto.getFirst(), userDto.getLast(), userDto.getSecondLast());
        if (createUserResult.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            throw new RuntimeException(
                    "Error creating user: " + createUserResult.getSdkHttpMetadata().getHttpStatusCode());

        }

        String externalId = createUserResult.getUser().getUsername();

        UserEntity user = new UserEntity();
        user.setExternalId(externalId);
        user.setName(new UserName(userDto.getFirst(), userDto.getLast(), userDto.getSecondLast()));
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setCreatedBy(1);
        userRepository.save(user);

        return new WebServiceResponse(true, "User created successfully");

    }

    @Transactional
    public WebServiceResponse updateUser(UserDTO userDto, Integer userId) {

            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setName(new UserName(userDto.getFirst(), userDto.getLast(), userDto.getSecondLast()));
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());
            user.setCreatedBy(1);
            userRepository.save(user);

            AdminUpdateUserAttributesResult updateUserAttributesResult = cognitoService.updateCognitoUserAttributes(
                    user.getExternalId(), userDto.getEmail(), userDto.getFirst(), userDto.getLast(), userDto.getSecondLast());

            if (updateUserAttributesResult.getSdkHttpMetadata().getHttpStatusCode() != 200)
                throw new RuntimeException(
                        "Error update user: " + updateUserAttributesResult.getSdkHttpMetadata().getHttpStatusCode());

            return new WebServiceResponse(true, "User updated successfully");

    }

    @Transactional
    public WebServiceResponse deleteUser(Integer userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));

        AdminUserGlobalSignOutResult signOutResult = cognitoService.adminSignOutCognitoUser(user.getExternalId());

        AdminDisableUserResult adminDisableUserResult = cognitoService.adminDisableCognitoUser(user.getExternalId());

        if (signOutResult.getSdkHttpMetadata().getHttpStatusCode() != 200)
            throw new RuntimeException("Error disabling user in Amazon Cognito: " + signOutResult.getSdkHttpMetadata().getHttpStatusCode());

        if (adminDisableUserResult.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            throw new RuntimeException("Error disabling user in Amazon Cognito: " + adminDisableUserResult.getSdkHttpMetadata().getHttpStatusCode());
        }

        return new WebServiceResponse(true, "User deleted successfully");
    }

}