package com.fastturtle.userauthenticationservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class User extends BaseModel {

    private String email;

    private String password;

    @ManyToMany
    private Set<Role> roles = new HashSet<>();  // bcz a person can have multiple roles(instructor, mentor, ta etc.)
                                                // in case we don't define a role, by default it will be empty set for this user

    // one user can have many roles
    // one role can be played by multiple people(many people can be instructors at the same time)
    // so M:M


}
