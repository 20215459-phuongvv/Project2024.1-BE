package com.hust.project3.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "borrow_books")
public class BorrowBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reading_card_id")
    private ReadingCard readingCard;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "is_late")
    private Boolean isLate;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "updatedBy")
    private String updatedBy;

    @Column(name = "status")
    private Integer status;
}
