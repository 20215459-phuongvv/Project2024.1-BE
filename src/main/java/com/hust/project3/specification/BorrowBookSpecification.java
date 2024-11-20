package com.hust.project3.specification;

import com.hust.project3.dtos.borrowing.BorrowBookRequestDTO;
import com.hust.project3.entities.BorrowBook;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BorrowBookSpecification {

    public static Specification<BorrowBook> byCriteria(BorrowBookRequestDTO dto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getStart() != null && dto.getEnd() != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("updatedTime"),
                        dto.getStart(),
                        dto.getEnd()
                ));
            }

            if (dto.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), dto.getStatus()));
            }

            if (dto.getIsLate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isLate"), dto.getIsLate()));
            }

            if (dto.getBookName() != null && !dto.getBookName().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.join("book").get("title")),
                        "%" + dto.getBookName().toLowerCase() + "%"
                ));
            }

            if (dto.getReadingCardCode() != null && !dto.getReadingCardCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.join("readingCard").get("code")),
                        "%" + dto.getReadingCardCode().toLowerCase() + "%"
                ));
            }

            if (dto.getUserEmail() != null && !dto.getUserEmail().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.join("readingCard").join("user").get("email")),
                        "%" + dto.getUserEmail().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

