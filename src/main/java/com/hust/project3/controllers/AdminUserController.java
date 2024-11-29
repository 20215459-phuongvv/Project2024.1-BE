package com.hust.project3.controllers;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.ResultMeta;
import com.hust.project3.dtos.user.UserQueryRequestDTO;
import com.hust.project3.dtos.user.UserRequestDTO;
import com.hust.project3.entities.User;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public Result getUsersByProperties(UserQueryRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Page<User> page = userService.getUsersByProperties(dto, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(userService.getUserById(id));
    }

    @PutMapping
    public Result updateUser(@RequestHeader("Authorization") String jwt,
                             @RequestBody UserRequestDTO dto) throws NotFoundException {
        return Result.ok(userService.updateUser(jwt, dto));
    }
}
