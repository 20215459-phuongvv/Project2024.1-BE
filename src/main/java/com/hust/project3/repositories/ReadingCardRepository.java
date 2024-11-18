package com.hust.project3.repositories;

import com.hust.project3.entities.ReadingCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReadingCardRepository extends JpaRepository<ReadingCard, Long>, JpaSpecificationExecutor<ReadingCard> {
}
