package com.hust.project3.specification;

import com.hust.project3.dtos.book.BookRequestDTO;
import com.hust.project3.entities.Book;
import com.hust.project3.enums.BookTypeEnum;
import com.hust.project3.enums.EntityStatusEnum;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> byCriteria(BookRequestDTO dto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getAuthorId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("author").get("id"), dto.getAuthorId()));
            }

            if (dto.getPublisherId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("publisher"), dto.getPublisherId()));
            }

            if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + dto.getTitle() + "%"));
            }

            if (dto.getIsAvailable() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isAvailable"), dto.getIsAvailable()));
            }

            if (dto.getIsVip() != null && dto.getIsVip()) {
                if (dto.getType() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), dto.getType()));
                } else {
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("type"), BookTypeEnum.NORMAL.ordinal()),
                            criteriaBuilder.equal(root.get("type"), BookTypeEnum.SPECIAL.ordinal())
                    ));
                }
            } else {
                predicates.add(criteriaBuilder.equal(root.get("type"), BookTypeEnum.NORMAL.ordinal()));
            }

            if (dto.getIsVip() == null) {
                if (dto.getStatus() != null) predicates.add(criteriaBuilder.equal(root.get("status"), dto.getStatus()));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("status"), EntityStatusEnum.ACTIVE.ordinal()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
