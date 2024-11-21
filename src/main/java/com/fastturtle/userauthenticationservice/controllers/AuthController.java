package com.fastturtle.userauthenticationservice.controllers;

import com.fastturtle.userauthenticationservice.dtos.ValidateTokenRequestDTO;
import com.fastturtle.userauthenticationservice.exceptions.InvalidTokenException;
import com.fastturtle.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.fastturtle.userauthenticationservice.dtos.LoginRequestDTO;
import com.fastturtle.userauthenticationservice.dtos.SignupRequestDTO;
import com.fastturtle.userauthenticationservice.dtos.UserDTO;
import com.fastturtle.userauthenticationservice.models.User;
import com.fastturtle.userauthenticationservice.services.IAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Pair<User, MultiValueMap<String, String>> userWithHeaders = authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        User user = userWithHeaders.a;
        if(user == null) {
            throw new BadCredentialsException("Bad credentials");
        }
        return new ResponseEntity<>(from(user), userWithHeaders.b, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public Boolean validateToken(@RequestBody ValidateTokenRequestDTO validateTokenRequestDTO) {
        Boolean response = authService.validateToken(validateTokenRequestDTO.getToken(), validateTokenRequestDTO.getUserId());

        if(!response) {
            throw new InvalidTokenException("Either token is stale or Invalid");
        }

        return true;
    }

    public UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

}
