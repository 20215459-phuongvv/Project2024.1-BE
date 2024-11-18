package com.hust.project3.controllers;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.ResultMeta;
import com.hust.project3.dtos.book.BookRequestDTO;
import com.hust.project3.entities.Book;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("api/vip/books")
public class VipBookController {
    private final BookService bookService;

    @GetMapping
    public Result getVipBooksByProperties(BookRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Page<Book> page = bookService.getVipBooksByProperties(dto, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @GetMapping("/{id}")
    public Result getVipBookById(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(bookService.getVipBookById(id));
    }
}
