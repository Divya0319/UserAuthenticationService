package com.fastturtle.userauthenticationservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Session extends BaseModel {

    @ManyToOne
    private User user;

    private String token;

    @Enumerated(EnumType.ORDINAL)
    private SessionState sessionState;

//   user -> session
//
//    1   :   m(active + inactive)  - 1 user can have many sessions
//    1   :   1 (1 session can have 1 user only)
}
