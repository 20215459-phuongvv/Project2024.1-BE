package com.hust.project3.dtos.user;

import lombok.Data;

@Data
public class UserQueryRequestDTO {
    private String keyword;
    private Integer status;
}
