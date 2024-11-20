package com.hust.project3.schedules;

import com.hust.project3.entities.BorrowBook;
import com.hust.project3.entities.ReadingCard;
import com.hust.project3.enums.BorrowBookStatusEnum;
import com.hust.project3.enums.ReadingCardStatusEnum;
import com.hust.project3.repositories.BorrowBookRepository;
import com.hust.project3.repositories.ReadingCardRepository;
import com.hust.project3.services.BorrowBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BorrowBookScheduler {
    private final BorrowBookService borrowBookService;
    private final BorrowBookRepository borrowBookRepository;
    private final ReadingCardRepository readingCardRepository;

    /**
     * Scheduled task to monitor borrowing status
     * Runs daily at 00:00
     */
    public void monitorBorrowingStatus() {
        System.out.println("Running borrowing status monitoring task at: " + LocalDate.now());

        // Fetch all borrowed books with NOT_RETURNED status
        List<BorrowBook> borrowings = borrowBookRepository.findAllByStatus(BorrowBookStatusEnum.NOT_RETURNED.ordinal());

        for (BorrowBook borrowing : borrowings) {
            LocalDate dueDate = borrowing.getReturnDate();
            LocalDate currentDate = LocalDate.now();

            if (currentDate.isAfter(dueDate)) {
                long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(dueDate, currentDate);
                borrowing.setOverDueDays(overdueDays);
                borrowing = borrowBookService.markAsOverdue(borrowing);
                ReadingCard readingCard = borrowing.getReadingCard();

                if (overdueDays > 30) {
                    readingCard.setStatus(ReadingCardStatusEnum.BANNED.ordinal());
                    readingCardRepository.save(readingCard);
                    continue;
                }

                if (overdueDays > 5) {
                    readingCard.setViolationCount(readingCard.getViolationCount() + 1);
                    if (readingCard.getViolationCount() >= 3) {
                        readingCard.setStatus(ReadingCardStatusEnum.BANNED.ordinal());
                    } else {
                        readingCard.setStatus(ReadingCardStatusEnum.WARNED.ordinal());
                    }
                    readingCardRepository.save(readingCard);
                }
            }
        }
        System.out.println("Borrowing status monitoring task completed.");
    }
}

