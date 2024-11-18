package com.hust.project3.controllers;

import com.hust.project3.dtos.auth.AuthRequestDTO;
import com.hust.project3.dtos.auth.AuthResponseDTO;
import com.hust.project3.dtos.user.UserRequestDTO;
import com.hust.project3.repositories.UserRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO user) throws Exception {
        AuthResponseDTO authResponseDTO= userService.createUser(user);
        return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDTO authRequestDTO) throws Exception{
        try {
            AuthResponseDTO authResponseDTO= userService.signIn(authRequestDTO);
            System.out.println(SecurityContextHolder.getContext());
            return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/verify-email/{token}")
    public ModelAndView confirmEmail(@PathVariable("token") String token) {
        try{
            String result = userService.confirmEmail(token);
            return new ModelAndView("confirm-success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("confirm-fail");
        }
    }
}
