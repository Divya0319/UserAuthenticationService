package com.fastturtle.userauthenticationservice.services;

import com.fastturtle.userauthenticationservice.models.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    @Override
    public User signUp(String email, String password) {
        return null;
    }

    @Override
    public User login(String email, String password) {
        return null;
    }
}
