package com.hust.project3.repositories;

import com.hust.project3.entities.BorrowBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long> {
}
