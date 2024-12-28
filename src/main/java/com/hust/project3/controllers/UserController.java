package com.hust.project3.controllers;

import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.auth.AuthRequestDTO;
import com.hust.project3.dtos.user.UserRequestDTO;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public Result getUserProfile(@RequestHeader("Authorization") String jwt) throws NotFoundException {
        return Result.ok(userService.findUserByJwt(jwt));
    }

    @PutMapping("/profile")
    public Result updateUserProfile(@RequestHeader("Authorization") String jwt,
                                    @RequestBody UserRequestDTO dto) throws NotFoundException {
        return Result.ok(userService.updateUserProfile(jwt, dto));
    }

    @PutMapping("/change-password")
    public Result changePassword(@RequestHeader("Authorization") String jwt,
                                 @RequestBody AuthRequestDTO dto) throws NotFoundException {
        return Result.ok(userService.changePassword(jwt, dto));
    }

    @PutMapping("/upgrade-vip")
    public Result upgradeVip() {
        return Result.ok(userService.upgradeVip());
    }

    @PutMapping("/request-payment")
    public Result requestPayment() {
        return Result.ok(userService.requestUpgradeVipPayment());
    }
}
