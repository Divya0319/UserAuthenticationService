package com.fastturtle.userauthenticationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDTO {

    private String email;

    private String password;
}
