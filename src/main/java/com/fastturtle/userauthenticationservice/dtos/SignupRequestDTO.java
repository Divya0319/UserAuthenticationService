package com.fastturtle.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDTO {

    private String email;

    private String password;
}
