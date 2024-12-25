package com.hust.project3.dtos.book;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BookRequestDTO {
    private Long id;
    private Long authorId;
    private Long publisherId;
    private String title;
    private Boolean isAvailable;
    private Integer type;
    private Boolean isVip;
    private MultipartFile thumbnail;
    private Integer status;
}
