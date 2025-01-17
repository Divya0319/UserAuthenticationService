package com.fastturtle.userauthenticationservice.controllers;

import com.fastturtle.userauthenticationservice.dtos.UserDTO;
import com.fastturtle.userauthenticationservice.models.User;
import com.fastturtle.userauthenticationservice.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public UserDTO getUserDetails(@PathVariable Long id) {
        User user = userService.getUser(id);
        System.out.println("USER: " + user.getEmail());
        return from(user);
    }

    public UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }
}
