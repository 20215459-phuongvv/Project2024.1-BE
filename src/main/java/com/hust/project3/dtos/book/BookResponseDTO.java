package com.hust.project3.dtos.book;

import com.hust.project3.entities.Author;
import com.hust.project3.entities.Publisher;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class BookResponseDTO {
    private Long id;
    private Author author;
    private Publisher publisher;
    private String title;
    private Integer type;
    private Boolean isAvailable;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Integer status;
    private Boolean isSubscribe;
}
