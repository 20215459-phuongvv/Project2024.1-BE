package com.hust.project3.specification;

import com.hust.project3.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return builder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("email")), likePattern),
                    builder.like(builder.lower(root.get("name")), likePattern),
                    builder.like(builder.lower(root.get("phone")), likePattern)
            );
        };
    }

    public static Specification<User> hasStatus(Integer status) {
        return (root, query, builder) -> {
            if (status == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("status"), status);
        };
    }
}

