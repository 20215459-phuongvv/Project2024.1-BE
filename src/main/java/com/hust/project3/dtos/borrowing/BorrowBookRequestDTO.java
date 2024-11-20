package com.hust.project3.dtos.borrowing;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowBookRequestDTO {
    private Long id;
    private Long bookId;
    private String bookName;
    private String readingCardCode;
    private String userEmail;
    private Long readingCardId;
    private String title;
    private Long numberOfDays;
    private Boolean isLate;
    private Integer status;
    private LocalDate start;
    private LocalDate end;
}
