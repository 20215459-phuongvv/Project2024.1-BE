package com.hust.project3.services;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.book.BookRequestDTO;
import com.hust.project3.dtos.book.BookResponseDTO;
import com.hust.project3.entities.Book;
import com.hust.project3.exceptions.NotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    Page<BookResponseDTO> getBooksByProperties(BookRequestDTO dto, PagingRequestDTO pagingRequestDTO);

    Book getBookById(Long id) throws NotFoundException;

    Book addBook(String jwt, BookRequestDTO dto) throws NotFoundException;

    Book updateBook(String jwt, BookRequestDTO dto) throws NotFoundException, MessagingException;

    Page<BookResponseDTO> getVipBooksByProperties(BookRequestDTO dto, PagingRequestDTO pagingRequestDTO);

    Book getVipBookById(Long id) throws NotFoundException;

    List<Book> deleteBooks(String jwt, List<Long> idList) throws NotFoundException;
}
