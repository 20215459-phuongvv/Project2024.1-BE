package com.hust.project3.repositories;

import com.hust.project3.entities.BorrowBook;
import com.hust.project3.entities.ReadingCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long>, JpaSpecificationExecutor<BorrowBook> {
    Page<BorrowBook> findByReadingCard(ReadingCard readingCard, Pageable pageable);

    List<BorrowBook> findAllByStatus(int ordinal);
    @Query("SELECT bb FROM BorrowBook bb WHERE bb.borrowDate BETWEEN :startOfMonth AND :endOfMonth")
    List<BorrowBook> findBorrowBookByBorrowDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
