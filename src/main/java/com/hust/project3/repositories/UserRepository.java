package com.hust.project3.repositories;

import com.hust.project3.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String username);
    Optional<User> findByVerificationToken(String token);
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByIdNotAndPhone(Long id, String phone);
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :start AND :end")
    List<User> findUsersByRegistrationDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
