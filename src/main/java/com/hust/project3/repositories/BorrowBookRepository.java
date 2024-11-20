package com.hust.project3.repositories;

import com.hust.project3.entities.BorrowBook;
import com.hust.project3.entities.ReadingCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long>, JpaSpecificationExecutor<BorrowBook> {
    Page<BorrowBook> findByReadingCardIn(List<ReadingCard> readingCardList, Pageable pageable);

    List<BorrowBook> findAllByStatus(int ordinal);
}
