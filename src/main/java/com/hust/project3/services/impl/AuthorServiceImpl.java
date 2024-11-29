package com.hust.project3.services.impl;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.author.AuthorRequestDTO;
import com.hust.project3.entities.Author;
import com.hust.project3.enums.EntityStatusEnum;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.AuthorRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.AuthorService;
import com.hust.project3.specification.AuthorSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Page<Author> getAuthorsByProperties(AuthorRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Pageable pageable = PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize(), Sort.by("id").descending());
        Specification<Author> spec = AuthorSpecification.byCriteria(dto);
        return authorRepository.findAll(spec, pageable);
    }

    @Override
    public Author getAuthorById(Long id) throws NotFoundException {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found"));
    }

    @Override
    public Author addAuthor(String jwt, AuthorRequestDTO dto) {
        Author author = Author.builder()
                .name(dto.getName())
                .updatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt))
                .build();
        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthor(String jwt, AuthorRequestDTO dto) throws NotFoundException {
        Author author = authorRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Author not found"));
        author.setName(dto.getName());
        author.setUpdatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt));
        return authorRepository.save(author);
    }

    @Override
    public List<Author> deleteAuthors(String jwt, List<Long> idList) throws NotFoundException {
        List<Author> result = new ArrayList<>();
        for (long id : idList) {
            Author author = getAuthorById(id);
            author.setUpdatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt));
            author.setStatus(EntityStatusEnum.INACTIVE.ordinal());
            result.add(authorRepository.save(author));
        }
        return result;
    }
}
