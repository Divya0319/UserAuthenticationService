package com.fastturtle.userauthenticationservice.services;

import com.fastturtle.userauthenticationservice.models.Role;
import com.fastturtle.userauthenticationservice.models.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.util.MultiValueMap;

import java.util.Set;

public interface IAuthService {

    User signUp(String email, String password, Set<Role> roles);

    Pair<User, MultiValueMap<String, String>> login(String email, String password);

    Boolean validateToken(String token, Long userId);
}
