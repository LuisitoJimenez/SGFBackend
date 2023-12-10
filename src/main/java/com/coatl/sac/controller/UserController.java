package com.coatl.sac.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.TeamEntity;
import com.coatl.sac.entity.UserEntity;
import com.coatl.sac.model.UserDTO;
import com.coatl.sac.repository.TeamRepository;
import com.coatl.sac.repository.UserRepository;
import com.coatl.sac.service.UserService;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Users management")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("")
    @Operation(summary = "Get all users")
    public WebServiceResponse getUsersList() {
        return new WebServiceResponse(userService.getUserList());
    }

    @GetMapping("/{userId}")
    public WebServiceResponse getUserById(
            @PathVariable Integer userId) {
        return new WebServiceResponse(userService.getUserById(userId));
    }

    @PostMapping("")
    public WebServiceResponse createUser(
            @RequestBody UserDTO UserDto) {
        return userService.createUser(UserDto);
    }

    @PatchMapping("/{userId}")
    public WebServiceResponse updateUser(
            @RequestBody UserDTO userDto,
            @PathVariable Integer userId) {
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public WebServiceResponse deleteUser(
            @PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }

    @PostMapping(value = "/{userId}/team/{teamId}")
    public WebServiceResponse assignTeam(
            @PathVariable Integer userId,
            @PathVariable Integer teamId) {
        return userService.assignTeam(userId, teamId);
    }

    @GetMapping(value = "/teams/{userId}")
    public WebServiceResponse getTeams(
            @PathVariable Integer userId) {
        return new WebServiceResponse(userService.getTeams(userId));
    }

    @DeleteMapping(value = "/{userId}/team/{teamId}")
    public WebServiceResponse removeTeam(
            @PathVariable Integer userId,
            @PathVariable Integer teamId) {
        return userService.removeTeam(userId, teamId);
    }

    @PostMapping(value = "/{userId}/role/{roleId}")
    public WebServiceResponse assignRole(
        @PathVariable Integer userId,
        @PathVariable Integer roleId,
        @RequestHeader Integer teamId
    ) {
        return new WebServiceResponse(userService.assigRole(userId, roleId, teamId));
    }

    @GetMapping(value = "/roles/{userId}")
    public WebServiceResponse getRoles(
        @PathVariable Integer userId
    ) {
        return new WebServiceResponse(userService.getRoles(userId));
    }

    @GetMapping("/test/{userId}/teams")
    public List<TeamEntity> getUserRoles(@PathVariable Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
                return userEntity.getTeams();
    }

    @GetMapping("/test/{teamId}/players")
    public List<UserEntity> getUserTeams(@PathVariable Integer teamId) {
        TeamEntity teamEntity = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
                return teamEntity.getUsers();
    }

}