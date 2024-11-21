package com.fastturtle.userauthenticationservice.services;

import com.fastturtle.userauthenticationservice.models.Session;
import com.fastturtle.userauthenticationservice.models.SessionState;
import com.fastturtle.userauthenticationservice.models.User;
import com.fastturtle.userauthenticationservice.repos.SessionRepo;
import com.fastturtle.userauthenticationservice.repos.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    private final UserRepo userRepo;

    private final SessionRepo sessionRepo;

//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SecretKey secretKey;

    public AuthService(UserRepo userRepo, SessionRepo sessionRepo,
//                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       SecretKey secretKey) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.secretKey = secretKey;
    }

    @Override
    public User signUp(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isPresent()) {
            return null;
        }

        User user = new User();
        user.setEmail(email);
//        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPassword(password);
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
//        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
//            return null;
//        }
        if(!user.getPassword().equals(password)) {
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
        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);

        Session session = new Session();
        session.setSessionState(SessionState.ACTIVE);
        session.setUser(user);
        session.setToken(token);
        sessionRepo.save(session);

        return new Pair<User, MultiValueMap<String, String>>(user, headers);


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

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();

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
