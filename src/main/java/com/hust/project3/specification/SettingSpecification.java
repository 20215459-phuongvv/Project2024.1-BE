package com.hust.project3.specification;

import com.hust.project3.dtos.setting.SettingRequestDTO;
import com.hust.project3.entities.Setting;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SettingSpecification {
    public static Specification<Setting> byCriteria(SettingRequestDTO dto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by name (approximate search)
            if (dto.getKey() != null && !dto.getKey().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("key"), "%" + dto.getKey() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
