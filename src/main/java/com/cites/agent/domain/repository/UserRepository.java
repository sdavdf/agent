package com.cites.agent.domain.repository;

import java.util.Optional;

import com.cites.agent.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    
}
