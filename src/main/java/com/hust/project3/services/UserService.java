package com.hust.project3.services;

import com.hust.project3.dtos.auth.AuthRequestDTO;
import com.hust.project3.dtos.auth.AuthResponseDTO;
import com.hust.project3.dtos.user.UserRequestDTO;
import com.hust.project3.exceptions.BadRequestException;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    AuthResponseDTO createUser(UserRequestDTO user) throws MessagingException, BadRequestException;

    AuthResponseDTO signIn(AuthRequestDTO authRequestDTO) throws BadRequestException;

    String confirmEmail(String token) throws Exception;

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
