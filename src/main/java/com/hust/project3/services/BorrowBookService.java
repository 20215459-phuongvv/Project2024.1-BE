package com.hust.project3.services;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.borrowing.BorrowBookRequestDTO;
import com.hust.project3.entities.BorrowBook;
import org.springframework.data.domain.Page;

public interface BorrowBookService {
    Page<BorrowBook> getBorrowingsByUserLogin(String jwt, BorrowBookRequestDTO dto, PagingRequestDTO pagingRequestDTO);

    BorrowBook getBorrowingById(String jwt, Long id);

    BorrowBook addBorrowBook(String jwt, BorrowBookRequestDTO dto);
}
