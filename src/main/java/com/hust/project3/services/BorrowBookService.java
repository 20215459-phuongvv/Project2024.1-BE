package com.hust.project3.services;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.borrowing.BorrowBookRequestDTO;
import com.hust.project3.entities.BorrowBook;
import com.hust.project3.exceptions.BadRequestException;
import com.hust.project3.exceptions.NotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

public interface BorrowBookService {
    Page<BorrowBook> getBorrowingsByUserLogin(String jwt, BorrowBookRequestDTO dto, PagingRequestDTO pagingRequestDTO) throws NotFoundException;

    BorrowBook getUserBorrowingById(String jwt, Long id);

    BorrowBook addBorrowBook(String jwt, BorrowBookRequestDTO dto) throws NotFoundException, BadRequestException;

    BorrowBook markAsOverdue(BorrowBook borrowing);

    Page<BorrowBook> getBorrowBooksByCriteria(BorrowBookRequestDTO dto, PagingRequestDTO pagingRequestDTO);

    BorrowBook getBorrowingById(Long id) throws NotFoundException;
    BorrowBook updateReturnedBorrowing(Long id) throws NotFoundException, MessagingException;
}
