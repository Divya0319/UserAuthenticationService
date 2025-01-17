package com.fastturtle.userauthenticationservice.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Role extends BaseModel {
    private String name;

    public Role() {

    }

    public Role(String name) {
        this.name = name;
    }
}
