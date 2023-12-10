package com.coatl.sac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.RoleEntity;
import com.coatl.sac.entity.UserEntity;
import com.coatl.sac.entity.UserTeamEntity;
import com.coatl.sac.entity.UserTeamRoleEntity;
import com.coatl.sac.json.UserName;
import com.coatl.sac.model.UserDTO;
import com.coatl.sac.repository.GenderRepository;
import com.coatl.sac.repository.RoleRepository;
import com.coatl.sac.repository.UserRepository;
import com.coatl.sac.repository.UserTeamRepository;
import com.coatl.sac.repository.UserTeamRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserResult;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesResult;
import com.amazonaws.services.cognitoidp.model.AdminUserGlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.AdminUserGlobalSignOutResult;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {

    // @Autowired
    private final UserRepository userRepository;

    // @Autowired
    private final UserTeamRepository userTeamRepository;

    // @Autowired
    private final UserTeamRoleRepository userTeamRoleRepository;

    // @Autowired
    private final RoleRepository roleRepository;

    // @Autowired
    private final GenderRepository genderRepository;

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
                /*
                 * if (modifiedUser.get("address") != null) {
                 * Map<String, Object> name =
                 * objectMapper.readValue(user.get("address").toString(), Map.class);
                 * modifiedUser.put("address", name);
                 * }
                 */
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
            /*
             * if (newMap.get("address") != null) {
             * newMap.put("address",
             * objectMapper.readValue(newMap.get("address").toString(), Map.class));
             * }
             */
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
        // user.setAddress(new Address(userDto.getStreet(), userDto.getPostalCode(),
        // userDto.getMunicipality(), userDto.getTown(), userDto.getState()));
        user.setUserCreated(1);
        // Date birthday = Date.valueOf(userDto.getBirthday());
        // user.setBirthday(birthday);
        userRepository.save(user);

        // AdminGetUserResponse adminGetUserResponse =
        // cognitoService.getAdminUser(userDto.getEmail());
        // cognitoService.createUser(userDto.getEmail(), userDto.getPassword(),
        // userDto.getFirst(), userDto.getLast(), userDto.getSecondLast());

        /*
         * GenderEntity genderEntity =
         * genderRepository.findByName(userDto.getGender()).orElseThrow(() -> new
         * RuntimeException("Gender not found"));
         * saveUserGender(user.getId(), genderEntity.getId());
         */

        return new WebServiceResponse(true, "User created successfully");

    }

    @Transactional
    public WebServiceResponse updateUser(UserDTO userDto, Integer userId) {

            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setName(new UserName(userDto.getFirst(), userDto.getLast(), userDto.getSecondLast()));
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());
            // user.setAddress(new Address(userDto.getStreet(), userDto.getPostalCode(),
            // userDto.getMunicipality(), userDto.getTown(), userDto.getState()));
            user.setUserCreated(1);
            // Date birthday = Date.valueOf(userDto.getBirthday());
            // user.setBirthday(birthday);
            userRepository.save(user);

            AdminUpdateUserAttributesResult updateUserAttributesResult = cognitoService.updateCognitoUserAttributes(
                    user.getExternalId(), userDto.getEmail(), userDto.getFirst(), userDto.getLast(), userDto.getSecondLast());

            if (updateUserAttributesResult.getSdkHttpMetadata().getHttpStatusCode() != 200)
                throw new RuntimeException(
                        "Error update user: " + updateUserAttributesResult.getSdkHttpMetadata().getHttpStatusCode());
            /*
             * Integer genderId =
             * genderRepository.findByName(userDto.getGender()).orElseThrow(()-> new
             * RuntimeException("Gender not found")).getId();
             * 
             * if (userGenderRepository.findByUserId(userId).isPresent()) {
             * UserGenderEntity userGenderEntity =
             * userGenderRepository.findByUserId(userId).orElseThrow(() -> new
             * RuntimeException("User not found"));
             * userGenderEntity.setGenderId(genderId);
             * userGenderRepository.save(userGenderEntity);
             * }
             */

            return new WebServiceResponse(true, "User updated successfully");

    }

    @Transactional
    public WebServiceResponse deleteUser(Integer userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(Timestamp.valueOf(LocalDateTime.now()));

        AdminUserGlobalSignOutResult signOutResult = cognitoService.adminSignOutCognitoUser(user.getExternalId());

        AdminDisableUserResult adminDisableUserResult = cognitoService.adminDisableCognitoUser(user.getExternalId());

        if (signOutResult.getSdkHttpMetadata().getHttpStatusCode() != 200)
            throw new RuntimeException("Error disabling user in Amazon Cognito: " + signOutResult.getSdkHttpMetadata().getHttpStatusCode());

        if (adminDisableUserResult.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            throw new RuntimeException("Error disabling user in Amazon Cognito: " + adminDisableUserResult.getSdkHttpMetadata().getHttpStatusCode());
        }

        return new WebServiceResponse(true, "User deleted successfully");
    }

    @Transactional
    public WebServiceResponse assignTeam(Integer userId, Integer teamId) {
        UserTeamEntity userTeamEntity = new UserTeamEntity();
        userTeamEntity.setUserId(userId);
        userTeamEntity.setTeamId(teamId);
        userTeamEntity.setUserCreated(1);
        userTeamRepository.save(userTeamEntity);
        return new WebServiceResponse(true, "User assigned to team successfully");
    }

    public List<Map<String, Object>> getTeams(Integer userId) {
        List<Map<String, Object>> userTeams = userRepository.getTeams(userId);
        return userTeams;
    }

    public List<Map<String, Object>> getRoles(Integer userId) {
        List<Map<String, Object>> userRoles = userRepository.getRoles(userId);
        return userRoles;
    }

    /*
     * public List<Map<String, Object>> getTeams(Integer userId) {
     * UserTeamEntity userTeamEntity = userTeamRepository.findByUserId(userId)
     * .orElseThrow(() -> new
     * RuntimeException("User has no associted soccer teams"));
     * List<Map<String, Object>> userTeams = userRepository.getTeams(userId);
     * List<Map<String, Object>> result = new ArrayList<>();
     * for (Map<String, Object> userTeam : userTeams) {
     * Map<String, Object> newUserTeam = new HashMap<>(userTeam);
     * try {
     * String coachString = (String) newUserTeam.get("coach");
     * Map<String, Object> coach = objectMapper.readValue(coachString, Map.class);
     * newUserTeam.put("coach", coach);
     * 
     * String nameUserString = (String) newUserTeam.get("nameUser");
     * Map<String, Object> nameUser = objectMapper.readValue(nameUserString,
     * Map.class);
     * newUserTeam.put("nameUser", nameUser);
     * } catch (IOException e) {
     * // Manejar excepción
     * }
     * result.add(newUserTeam);
     * }
     * return result;
     * }
     */

    @Transactional
    public WebServiceResponse removeTeam(Integer userId, Integer teamId) {
        UserTeamEntity userTeamEntity = userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new RuntimeException("Team with user not found"));
        userTeamEntity.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
        userTeamRepository.save(userTeamEntity);
        return new WebServiceResponse(true, "Team removed successfully");
    }

    @Transactional
    public WebServiceResponse assigRole(Integer userId, Integer roleId, Integer teamId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        UserTeamEntity userTeamEntity = userTeamRepository.findByUserIdAndTeamId(userEntity.getId(), teamId)
                .orElseThrow(() -> new RuntimeException("Team with user not found"));
        UserTeamRoleEntity userTeamRoleEntity = new UserTeamRoleEntity();
        userTeamRoleEntity.setRoleId(roleId);
        userTeamRoleEntity.setUserTeamId(userTeamEntity.getId());
        userTeamRoleEntity.setUserCreated(1);
        userTeamRoleRepository.save(userTeamRoleEntity);
        return new WebServiceResponse(true, "Rol assigned to team successfully");
    }

}