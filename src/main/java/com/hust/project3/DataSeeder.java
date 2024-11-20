package com.hust.project3;

import com.hust.project3.entities.User;
import com.hust.project3.enums.EntityStatusEnum;
import com.hust.project3.enums.RoleEnum;
import com.hust.project3.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void run(String...args) {
        String adminUsername = "admin@gmail.com";
        if (userRepository.existsByEmail("admin@gmail.com")) {
            return;
        }
        User admin = User.builder()
                .email(adminUsername)
                .password(passwordEncoder.encode("admin"))
                .name("Admin")
                .address("Hà Nội")
                .phone("0123456789")
                .status(EntityStatusEnum.ACTIVE.ordinal())
                .verificationToken(UUID.randomUUID().toString())
                .role(RoleEnum.ADMIN.name())
                .build();
        userRepository.save(admin);
    }
}
