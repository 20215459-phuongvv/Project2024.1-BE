package com.hust.project3.dtos;

import lombok.Data;

@Data
public class PagingRequestDTO {
    int page;
    int size = 10;
}
