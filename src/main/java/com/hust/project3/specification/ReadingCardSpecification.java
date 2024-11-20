package com.hust.project3.specification;

import com.hust.project3.dtos.readingCard.ReadingCardRequestDTO;
import com.hust.project3.entities.ReadingCard;
import com.hust.project3.entities.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReadingCardSpecification {

    public static Specification<ReadingCard> byCriteria(ReadingCardRequestDTO dto, User user) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("user"), user));
            if (dto.getCode() != null && !dto.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("code"), "%" + dto.getCode() + "%"));
            }

            if (dto.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), dto.getType()));
            }

            if (dto.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), dto.getStartDate()));
            }

            if (dto.getExpiryDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("expiryDate"), dto.getExpiryDate()));
            }

            if (dto.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), dto.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ReadingCard> byCriteria(ReadingCardRequestDTO dto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getCode() != null && !dto.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("code")),
                        "%" + dto.getCode().toLowerCase() + "%"
                ));
            }

            if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("user").get("email")),
                        "%" + dto.getCode().toLowerCase() + "%"
                ));
            }

            if (dto.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), dto.getStatus()));
            }



            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

