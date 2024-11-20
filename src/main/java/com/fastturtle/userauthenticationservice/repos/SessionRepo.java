package com.fastturtle.userauthenticationservice.repos;

import com.fastturtle.userauthenticationservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepo extends JpaRepository<Session, Long> {
}
