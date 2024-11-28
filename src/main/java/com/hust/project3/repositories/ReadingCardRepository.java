package com.hust.project3.repositories;

import com.hust.project3.entities.ReadingCard;
import com.hust.project3.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ReadingCardRepository extends JpaRepository<ReadingCard, Long>, JpaSpecificationExecutor<ReadingCard> {
    Optional<ReadingCard> findByUserAndId(User user, Long id);
}
