package com.fastturtle.userauthenticationservice.services;

import com.fastturtle.userauthenticationservice.models.User;

public interface IAuthService {

    User signUp(String email, String password);

    User login(String email, String password);
}
