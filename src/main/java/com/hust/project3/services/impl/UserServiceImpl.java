package com.hust.project3.services.impl;

import com.hust.project3.dtos.auth.AuthRequestDTO;
import com.hust.project3.dtos.auth.AuthResponseDTO;
import com.hust.project3.dtos.user.UserRequestDTO;
import com.hust.project3.entities.User;
import com.hust.project3.enums.EntityStatusEnum;
import com.hust.project3.enums.RoleEnum;
import com.hust.project3.exceptions.BadRequestException;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.UserRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.EmailService;
import com.hust.project3.services.UserService;
import com.hust.project3.utils.Constants;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.UUID;

import static com.hust.project3.utils.Constants.messages;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
    @Override
    public AuthResponseDTO createUser(UserRequestDTO dto) throws MessagingException {
        User user =  User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(RoleEnum.USER.toString())
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .verificationToken(UUID.randomUUID().toString())
                .status(EntityStatusEnum.INACTIVE.ordinal())
                .build();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Context context = new Context();
        String url = Constants.LOCAL_HOST + "/auth/verify-email/" + user.getVerificationToken();
        context.setVariable("url", url);
        emailService.sendEmailWithHtmlTemplate(dto.getEmail(), messages.getString("email.verify"), "email-verification", context);
        return new AuthResponseDTO(null, true);
    }

    @Override
    public AuthResponseDTO signIn(AuthRequestDTO authRequestDTO) throws BadRequestException {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        String username = authRequestDTO.getEmail();
        String password = authRequestDTO.getPassword();
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateToken(authentication);
        authResponseDTO.setStatus(true);
        authResponseDTO.setJwt(accessToken);
        return authResponseDTO;
    }

    @Override
    public String confirmEmail(String token) throws Exception {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new NotFoundException(messages.getString("user.validate.not-found")));
        if(!token.equals(user.getVerificationToken())){
            userRepository.delete(user);
            throw new BadRequestException(messages.getString("user.validate.token-invalid"));
        }
        user.setStatus(EntityStatusEnum.ACTIVE.ordinal());
        userRepository.save(user);
        return messages.getString("email.verify.success");
    }

    private Authentication authenticate(String username, String password) throws BadRequestException {
        UserDetails userDetails = loadUserByUsername(username);
        System.out.println(userDetails);
        if(userDetails == null) {
            throw new BadCredentialsException(messages.getString("username.validate.invalid"));
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(messages.getString("password.validate.invalid"));
        }
        User user = userRepository.findByEmail(username).get();
        if(user.getStatus() != 1) {
            throw new BadRequestException(messages.getString("account.validate.not-verified"));
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
