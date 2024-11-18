package com.hust.project3.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String phone;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
}
