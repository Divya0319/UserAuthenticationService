package com.fastturtle.userauthenticationservice.dto;

import com.fastturtle.userauthenticationservice.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String email;
    private Role role;
}
