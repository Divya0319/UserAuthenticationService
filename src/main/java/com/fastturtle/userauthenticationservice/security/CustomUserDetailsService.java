package com.fastturtle.userauthenticationservice.security;

import com.fastturtle.userauthenticationservice.models.User;
import com.fastturtle.userauthenticationservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepo.findByEmail(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Bad credentials");
        }

        User user = userOptional.get();
        return new CustomUserDetails(user);

    }
}
