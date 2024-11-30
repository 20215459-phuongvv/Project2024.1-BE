package com.hust.project3.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String jwt;
    private boolean status;
    private Long userId; // Thêm ID của user
    private String username; // Thêm tên người dùng
    private String address;
    private String phone;
    private String email; // Thêm email người dùng
    private String role; // Thêm vai trò người dùng
}