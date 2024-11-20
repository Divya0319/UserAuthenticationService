package com.fastturtle.userauthenticationservice.services;

import com.fastturtle.userauthenticationservice.models.User;
import com.fastturtle.userauthenticationservice.repos.UserRepo;
import io.jsonwebtoken.Jwts;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    private final UserRepo userRepo;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User signUp(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isPresent()) {
            return null;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(user);

        return user;
    }

    @Override
    public Pair<User, MultiValueMap<String, String>> login(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        // Token generation

        String message = "{\n" +
                "  \"email\": \"anurag@scaler.com\",\n" +
                " \"roles\": [\n" +
                "   \"instructor\",\n" +
                "   \"buddy\"\n" +
                " ],\n" +
                " \"expirationDate\": \"25thJuly2024\"\n" +
                "}";

        byte[] content = message.getBytes(StandardCharsets.UTF_8);
        String token = Jwts.builder().content(content).compact();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);

        return new Pair<User, MultiValueMap<String, String>>(user, headers);


    }
}
