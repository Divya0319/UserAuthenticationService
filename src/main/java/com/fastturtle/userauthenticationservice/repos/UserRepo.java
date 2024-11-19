package com.fastturtle.userauthenticationservice.repos;

import com.fastturtle.userauthenticationservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
}
