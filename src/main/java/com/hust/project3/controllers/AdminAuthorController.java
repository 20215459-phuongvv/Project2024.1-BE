package com.hust.project3.controllers;

import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.author.AuthorRequestDTO;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/admin/authors")
public class AdminAuthorController {
    private final AuthorService authorService;

    @PostMapping
    public Result addAuthor(@RequestHeader("Authorization") String jwt,
                            @RequestBody @Valid AuthorRequestDTO dto) {
        return Result.ok(authorService.addAuthor(jwt, dto));
    }

    @PutMapping
    public Result updateAuthor(@RequestHeader("Authorization") String jwt,
                               @RequestBody @Valid AuthorRequestDTO dto) throws NotFoundException {
        return Result.ok(authorService.updateAuthor(jwt, dto));
    }

    @DeleteMapping
    public Result deleteAuthors(@RequestHeader("Authorization") String jwt,
                                @RequestBody List<Long> idList) throws NotFoundException {
        return Result.ok(authorService.deleteAuthors(jwt, idList));
    }
}
