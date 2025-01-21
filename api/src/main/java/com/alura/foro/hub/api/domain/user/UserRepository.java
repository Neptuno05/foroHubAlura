package com.alura.foro.hub.api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);
//    Optional<User> findByEmail(String email);
    UserDetails findByNameContainingIgnoreCase(String name);
    UserDetails findByName(String name);
}
