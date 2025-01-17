package com.fastturtle.userauthenticationservice.dtos;

import com.fastturtle.userauthenticationservice.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequestDTO {

    private String email;

    private String password;

    private Set<Role> roles;
}
