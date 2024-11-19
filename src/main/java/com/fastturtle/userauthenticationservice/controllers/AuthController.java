package com.fastturtle.userauthenticationservice.controllers;

import com.fastturtle.userauthenticationservice.dto.LoginRequestDTO;
import com.fastturtle.userauthenticationservice.dto.SignupRequestDTO;
import com.fastturtle.userauthenticationservice.dto.UserDTO;
import com.fastturtle.userauthenticationservice.models.User;
import com.fastturtle.userauthenticationservice.services.IAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    //Sign up
    //Login
    //ForgotPassword
    //Logout
    //...

    @PostMapping("/signup")
    public UserDTO signUp(@RequestBody SignupRequestDTO signupRequestDTO) {
        User user = authService.signUp(signupRequestDTO.getEmail(), signupRequestDTO.getPassword());
        return from(user);
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        return from(user);
    }

    public UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

}
