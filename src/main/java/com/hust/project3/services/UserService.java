package com.hust.project3.services;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.auth.AuthRequestDTO;
import com.hust.project3.dtos.auth.AuthResponseDTO;
import com.hust.project3.dtos.user.UserQueryRequestDTO;
import com.hust.project3.dtos.user.UserRequestDTO;
import com.hust.project3.entities.User;
import com.hust.project3.exceptions.BadRequestException;
import com.hust.project3.exceptions.NotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    AuthResponseDTO createUser(UserRequestDTO user) throws MessagingException, BadRequestException;

    AuthResponseDTO signIn(AuthRequestDTO authRequestDTO) throws BadRequestException;

    String confirmEmail(String token) throws Exception;

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User findUserByJwt(String jwt) throws NotFoundException;

    User updateUserProfile(String jwt, UserRequestDTO dto) throws NotFoundException;

    User changePassword(String jwt, AuthRequestDTO dto) throws NotFoundException;

    Page<User> getUsersByProperties(UserQueryRequestDTO dto, PagingRequestDTO pagingRequestDTO);

    User getUserById(Long id) throws NotFoundException;

    User updateUser(String jwt, UserRequestDTO dto) throws NotFoundException;
}
