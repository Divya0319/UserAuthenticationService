package com.fastturtle.userauthenticationservice.controllers;

import com.fastturtle.userauthenticationservice.UserAlreadyExistsException;
import com.fastturtle.userauthenticationservice.dto.LoginRequestDTO;
import com.fastturtle.userauthenticationservice.dto.SignupRequestDTO;
import com.fastturtle.userauthenticationservice.dto.UserDTO;
import com.fastturtle.userauthenticationservice.models.User;
import com.fastturtle.userauthenticationservice.services.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<UserDTO> signUp(@RequestBody SignupRequestDTO signupRequestDTO) {

        User user = authService.signUp(signupRequestDTO.getEmail(), signupRequestDTO.getPassword());

        if(user == null) {
            throw new UserAlreadyExistsException("User Already exists. Please try a different email");
        } else {
            return new ResponseEntity<>(from(user), HttpStatus.CREATED);
        }


    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        if(user == null) {
            throw new BadCredentialsException("Bad credentials");
        }
        return from(user);
    }

    public UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

}
