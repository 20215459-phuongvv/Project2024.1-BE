package com.hust.project3.controllers;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.ResultMeta;
import com.hust.project3.dtos.author.AuthorRequestDTO;
import com.hust.project3.entities.Author;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.AuthorService;
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
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public Result getAuthorsByProperties(AuthorRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Page<Author> page = authorService.getAuthorsByProperties(dto, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @GetMapping("/{id}")
    public Result getAuthorById(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(authorService.getAuthorById(id));
    }
}
