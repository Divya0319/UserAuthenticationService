package com.fastturtle.userauthenticationservice.controllers;

import com.fastturtle.userauthenticationservice.dto.LoginRequestDTO;
import com.fastturtle.userauthenticationservice.dto.SignupRequestDTO;
import com.fastturtle.userauthenticationservice.dto.UserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    //Sign up
    //Login
    //ForgotPassword
    //Logout
    //...

    @PostMapping("/signup")
    public UserDTO signUp(@RequestBody SignupRequestDTO signupRequestDTO) {
        return null;
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return null;
    }

}
