package com.coatl.sac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.model.UserDTO;
import com.coatl.sac.service.UserService;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Users management")
public class UserController {

    @Autowired
    private UserService userService;

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

    @GetMapping(value = "/teams/{userId}")
    public WebServiceResponse getTeams(
            @PathVariable Integer userId) {
        return new WebServiceResponse(userService.getTeams(userId));
    }

    @GetMapping(value = "/roles/{userId}")
    public WebServiceResponse getRoles(
        @PathVariable Integer userId
    ) {
        return new WebServiceResponse(userService.getRoles(userId));
    }

}