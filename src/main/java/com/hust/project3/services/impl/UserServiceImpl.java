package com.hust.project3.services.impl;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.auth.AuthRequestDTO;
import com.hust.project3.dtos.auth.AuthResponseDTO;
import com.hust.project3.dtos.payment.PaymentDTO;
import com.hust.project3.dtos.user.UserQueryRequestDTO;
import com.hust.project3.dtos.user.UserRequestDTO;
import com.hust.project3.entities.User;
import com.hust.project3.enums.EntityStatusEnum;
import com.hust.project3.enums.RoleEnum;
import com.hust.project3.exceptions.BadRequestException;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.UserRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.EmailService;
import com.hust.project3.services.PaymentService;
import com.hust.project3.services.UserService;
import com.hust.project3.specification.UserSpecification;
import com.hust.project3.utils.Constants;
import com.hust.project3.utils.SecurityUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.hust.project3.utils.Constants.messages;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PaymentService paymentService;

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
    public User findUserByJwt(String jwt) throws NotFoundException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User updateUserProfile(String jwt, UserRequestDTO dto) throws NotFoundException {
        User user = findUserByJwt(jwt);
        if (userRepository.existsByIdNotAndPhone(user.getId(), dto.getPhone())) {
            throw new NotFoundException("Phone number already exists");
        }
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        return userRepository.save(user);
    }

    @Override
    public User changePassword(String jwt, AuthRequestDTO dto) throws NotFoundException {
        User user = findUserByJwt(jwt);
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(messages.getString("password.validate.invalid"));
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        return userRepository.save(user);
    }

    @Override
    public Page<User> getUsersByProperties(UserQueryRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Pageable pageable = PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize());
        Specification<User> specs = Specification
                .where(UserSpecification.hasKeyword(dto.getKeyword()))
                .and(UserSpecification.hasStatus(dto.getStatus()));
        return userRepository.findAll(specs, pageable);
    }

    @Override
    public User getUserById(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User updateUser(String jwt, UserRequestDTO dto) throws NotFoundException {
        User user = getUserById(dto.getId());
        user.setStatus(dto.getStatus());
        user.setName(dto.getName());
        return userRepository.save(user);
    }

    @Override
    public List<User> getRecentUsers(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atTime(LocalTime.MIN);
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return userRepository.findUsersByRegistrationDate(start, end);
    }

    @Override
    public User upgradeVip() {
        User user = userRepository.findByEmail(SecurityUtil.getUserEmail()).get();
        user.setRole(RoleEnum.VIP_USER.name());
        return userRepository.save(user);
    }

    @Override
    public PaymentDTO.VNPayResponse requestUpgradeVipPayment() {
        return paymentService.createVipVnPayPayment();
    }

    @Override
    public AuthResponseDTO createUser(UserRequestDTO dto) throws MessagingException, BadRequestException {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new BadRequestException("Phone number already exists");
        }
        User user = User.builder()
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
        emailService.sendEmailWithHtmlTemplate(dto.getEmail(), messages.getString("email.verify"), "email-verification",
                context);
        return new AuthResponseDTO(
                null,
                true,
                user.getId(),
                user.getName(),
                user.getAddress(),
                user.getPhone(),
                user.getEmail(),
                user.getRole());
    }

    @Override
    public AuthResponseDTO signIn(AuthRequestDTO authRequestDTO) throws BadRequestException {
        String username = authRequestDTO.getEmail();
        String password = authRequestDTO.getPassword();

        // Xác thực thông tin đăng nhập
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Lấy thông tin user từ cơ sở dữ liệu
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(messages.getString("username.validate.invalid")));

        // Tạo JWT token
        String accessToken = jwtTokenProvider.generateToken(authentication);

        // Trả về AuthResponseDTO với thông tin người dùng
        return new AuthResponseDTO(
                accessToken,
                true,
                user.getId(),
                user.getName(),
                user.getAddress(),
                user.getPhone(),
                user.getEmail(),
                user.getRole());
    }

    @Override
    public String confirmEmail(String token) throws Exception {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new NotFoundException(messages.getString("user.validate.not-found")));
        if (!token.equals(user.getVerificationToken())) {
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
        if (userDetails == null) {
            throw new BadCredentialsException(messages.getString("username.validate.invalid"));
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(messages.getString("password.validate.invalid"));
        }
        User user = userRepository.findByEmail(username).get();
        if (user.getStatus() != 1) {
            throw new BadRequestException(messages.getString("account.validate.not-verified"));
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}