package com.hust.project3.services;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.author.AuthorRequestDTO;
import com.hust.project3.entities.Author;
import com.hust.project3.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorService {
    Page<Author> getAuthorsByProperties(AuthorRequestDTO dto, PagingRequestDTO pagingRequestDTO);

    Author getAuthorById(Long id) throws NotFoundException;

    Author addAuthor(String jwt, AuthorRequestDTO dto);

    Author updateAuthor(String jwt, AuthorRequestDTO dto) throws NotFoundException;

    List<Author> deleteAuthors(String jwt, List<Long> idList) throws NotFoundException;
}
