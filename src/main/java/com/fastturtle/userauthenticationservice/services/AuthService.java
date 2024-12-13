package com.fastturtle.userauthenticationservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastturtle.userauthenticationservice.clients.KafkaProducerClient;
import com.fastturtle.userauthenticationservice.dtos.EmailDTO;
import com.fastturtle.userauthenticationservice.models.Session;
import com.fastturtle.userauthenticationservice.models.SessionState;
import com.fastturtle.userauthenticationservice.models.User;
import com.fastturtle.userauthenticationservice.repos.SessionRepo;
import com.fastturtle.userauthenticationservice.repos.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    private final UserRepo userRepo;

    private final SessionRepo sessionRepo;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final KafkaProducerClient kafkaProducerClient;

    private final ObjectMapper objectMapper;

    @Value("${secretKey}")
    private String secretKey;

    public AuthService(UserRepo userRepo, SessionRepo sessionRepo, BCryptPasswordEncoder bCryptPasswordEncoder, KafkaProducerClient kafkaProducerClient, ObjectMapper objectMapper) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.kafkaProducerClient = kafkaProducerClient;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void postConstruct() {
        System.out.println(secretKey);
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
        try {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setTo(email);
            emailDTO.setSubject("Welcome to FastTurtle");
            String[] result = email.split("@");
            emailDTO.setBody(getEmailBody(result[0]));
            emailDTO.setFrom("divygupta0319@gmail.com");

            kafkaProducerClient.sendMessage("signup", objectMapper.writeValueAsString(emailDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    private String getEmailBody(String username) {

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>FastTurtle - Transforming Lives</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; color: #333;\">\n" +
                "    <div style=\"max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #3f51b5; color: #ffffff; padding: 20px; text-align: center;\">\n" +
                "            <h1 style=\"margin: 0; font-size: 24px;\">Welcome to FastTurtle</h1>\n" +
                "        </div>\n" +
                "\n" +
                "        <div style=\"padding: 20px;\">\n" +
                "            <p style=\"font-size: 16px; line-height: 1.6;\">Hi " + username + ",</p>\n" +

                "       <img src=\"https://i.imgur.com/HxcZEQ7.png\" alt=\"Imgur\" width=\"432\" height=\"577\">"+
                "\n" +
                "            <p style=\"font-size: 16px; line-height: 1.6;\">We're excited to have you on board! Here at FastTurtle, we strive to provide you with the best experience. Below is a quick overview of what we have to offer:</p>\n" +
                "\n" +
                "            <ul style=\"font-size: 16px; line-height: 1.6;\">\n" +
                "                <li> 1: Art Of Living.</li>\n" +
                "                <li> 2: Live like there is no tomorrow.</li>\n" +
                "                <li> 3: Glide like an Eagle.</li>\n" +
                "            </ul>\n" +
                "\n" +
                "            <p style=\"font-size: 16px; line-height: 1.6;\">Click the button below to explore your dashboard and get started:</p>\n" +
                "\n" +
                "            <div style=\"text-align: center; margin: 20px 0;\">\n" +
                "                <a href=\"https://codewithdivya.tech\" target=\"_blank\" style=\"background-color: #3f51b5; color: #ffffff; text-decoration: none; padding: 12px 24px; border-radius: 4px; font-size: 16px; display: inline-block;\">Get Started</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <p style=\"font-size: 16px; line-height: 1.6;\">If you have any questions, feel free to <a href=\"mailto:divygupta0319@gmail.com\" style=\"color: #3f51b5; text-decoration: none;\">reach out to us</a>. We're here to help!</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <div style=\"background-color: #f5f5f5; text-align: center; padding: 10px 20px; font-size: 12px; color: #666;\">\n" +
                "            <p style=\"margin: 0;\">&copy; 2024 FastTurtle. All rights reserved.</p>\n" +
                //"            <p style=\"margin: 0;\"><a href=\"[Privacy Policy Link]\" style=\"color: #3f51b5; text-decoration: none;\">Privacy Policy</a> | <a href=\"[Unsubscribe Link]\" style=\"color: #3f51b5; text-decoration: none;\">Unsubscribe</a></p>\n" +
                "        </div>\n" +

                "        <div style=\"margin-left: 60px\">\n" +
                "           <p style=\"font-size: 16px; line-height: 1.6;\"><i>This is a system generated mail. Please do not reply</i></p>\n" +
                "        </div>"+
                "    </div>\n" +
                "</body>\n" +
                "</html>";

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

//        String message = "{\n" +
//                "  \"email\": \"anurag@scaler.com\",\n" +
//                " \"roles\": [\n" +
//                "   \"instructor\",\n" +
//                "   \"buddy\"\n" +
//                " ],\n" +
//                " \"expirationDate\": \"25thJuly2024\"\n" +
//                "}";

        // Generating user data dynamically
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id__", user.getId());
        claims.put("roles", user.getRoles());
        claims.put("email", user.getEmail());
        long nowInMillis = System.currentTimeMillis();
        claims.put("iat", nowInMillis);
        claims.put("exp", nowInMillis + 1000000);

        //byte[] content = message.getBytes(StandardCharsets.UTF_8);
        //MacAlgorithm algorithm = Jwts.SIG.HS256;
        //SecretKey secretKey = algorithm.key().build();  // everytime we run these two lines, it will generate a unique and new secret key
        //String token = Jwts.builder().content(content).signWith(secretKey).compact();

        String token = Jwts.builder().claims(claims).signWith(generateKey()).compact();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);

        Session session = new Session();
        session.setSessionState(SessionState.ACTIVE);
        session.setUser(user);
        session.setToken(token);
        sessionRepo.save(session);

        return new Pair<User, MultiValueMap<String, String>>(user, headers);


    }

    private SecretKey generateKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        return secretKey;
    }

    @Override
    public Boolean validateToken(String token, Long userId) {

        Optional<Session> optionalSession = sessionRepo.findByToken(token);
        if(optionalSession.isEmpty()) {
            System.out.println("Token mismatch");
            return false;
        }

        Session session = optionalSession.get();

        String storedToken = session.getToken();

        JwtParser jwtParser = Jwts.parser().verifyWith(generateKey()).build();

        Claims claims = jwtParser.parseSignedClaims(storedToken).getPayload();

        Long tokenExpiry = (Long) claims.get("exp");

        Long currentTime = System.currentTimeMillis();

        System.out.println(tokenExpiry);
        System.out.println(currentTime);

        if(currentTime > tokenExpiry) {
            System.out.println("Token is expired");
            //set state to expired in my db
            return false;
        }

        // till this point it's good to go

        User user = userRepo.findById(userId).get();
        String email = user.getEmail();
        String tokenEmail = (String) claims.get("email");

        if(!email.equals(tokenEmail)) {
            System.out.println("Email mismatch");

            return false;
        }

        return true;

    }

    // TOKEN VALIDATION
    //exact token which we are getting in request - match with
    // token stored into our database

    // decrypt(unassemble) token - to get expiry
    // if it's not expired - then it's valid
}
