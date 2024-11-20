package com.hust.project3.controllers;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.ResultMeta;
import com.hust.project3.dtos.borrowing.BorrowBookRequestDTO;
import com.hust.project3.entities.BorrowBook;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.BorrowBookService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/borrow")
public class AdminBorrowBookController {
    private final BorrowBookService borrowBookService;

    @GetMapping
    public Result getBorrowings(BorrowBookRequestDTO dto,
                                PagingRequestDTO pagingRequestDTO) {
        Page<BorrowBook> page = borrowBookService.getBorrowBooksByCriteria(dto, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @GetMapping("/{id}")
    public Result getBorrowingById(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(borrowBookService.getBorrowingById(id));
    }

    @PutMapping("/{id}")
    public Result updateBorrowing(@PathVariable("id") Long id) throws MessagingException, NotFoundException {
        return Result.ok(borrowBookService.updateReturnedBorrowing(id));
    }
}
