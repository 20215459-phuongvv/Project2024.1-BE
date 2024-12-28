package com.hust.project3.dtos.readingCard;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReadingCardRequestDTO {
    private Long id;
    private Long userId;
    private String email;
    private String code;
    private Integer type;
    private Integer numberOfPeriod;
    private Integer status;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private Integer isRegistering;
}
