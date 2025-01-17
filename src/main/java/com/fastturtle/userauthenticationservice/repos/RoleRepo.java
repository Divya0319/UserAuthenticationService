package com.fastturtle.userauthenticationservice.repos;

import com.fastturtle.userauthenticationservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
