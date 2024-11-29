package com.hust.project3.repositories;

import com.hust.project3.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
    Optional<User> findByVerificationToken(String token);
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
