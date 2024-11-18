package com.hust.project3.specification;

import com.hust.project3.dtos.author.AuthorRequestDTO;
import com.hust.project3.entities.Author;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AuthorSpecification {

    public static Specification<Author> byCriteria(AuthorRequestDTO dto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getName() != null && !dto.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + dto.getName() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}