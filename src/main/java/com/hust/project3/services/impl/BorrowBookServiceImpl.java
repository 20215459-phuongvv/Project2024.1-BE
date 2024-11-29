package com.hust.project3.services.impl;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.borrowing.BorrowBookRequestDTO;
import com.hust.project3.entities.Book;
import com.hust.project3.entities.BorrowBook;
import com.hust.project3.entities.ReadingCard;
import com.hust.project3.entities.User;
import com.hust.project3.enums.BorrowBookStatusEnum;
import com.hust.project3.enums.ReadingCardStatusEnum;
import com.hust.project3.enums.RoleEnum;
import com.hust.project3.exceptions.BadRequestException;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.BookRepository;
import com.hust.project3.repositories.BorrowBookRepository;
import com.hust.project3.repositories.ReadingCardRepository;
import com.hust.project3.repositories.UserRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.BorrowBookService;
import com.hust.project3.services.NotificationService;
import com.hust.project3.specification.BorrowBookSpecification;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BorrowBookServiceImpl implements BorrowBookService {
    private final BorrowBookRepository borrowBookRepository;
    private final UserRepository userRepository;
    private final ReadingCardRepository readingCardRepository;
    private final BookRepository bookRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NotificationService notificationService;

    @Override
    public Page<BorrowBook> getBorrowingsByUserLogin(String jwt, BorrowBookRequestDTO dto, PagingRequestDTO pagingRequestDTO) throws NotFoundException {
        Pageable pageable = PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize(), Sort.by("id").descending());
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email).get();
        ReadingCard readingCard = user.getReadingCard();
        return borrowBookRepository.findByReadingCard(readingCard, pageable);
    }

    @Override
    public BorrowBook getUserBorrowingById(String jwt, Long id) throws NotFoundException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email).get();
        ReadingCard readingCard = user.getReadingCard();
        BorrowBook borrowBook = borrowBookRepository.findById(id).orElse(null);
        if (borrowBook != null && Objects.equals(readingCard.getId(), borrowBook.getReadingCard().getId())) {
            return borrowBook;
        }
        throw new NotFoundException("Borrow book not found");
    }

    @Override
    public BorrowBook addBorrowBook(String jwt, BorrowBookRequestDTO dto) throws NotFoundException, BadRequestException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email).get();
        ReadingCard readingCard = user.getReadingCard();
        if (readingCard.getStatus() == ReadingCardStatusEnum.BANNED.ordinal()) {
            throw new BadRequestException("This card is banned, contact admin");
        }
        if (Objects.equals(user.getRole(), RoleEnum.USER.name())) {
            if (dto.getNumberOfDays() > 45) throw new BadRequestException("Cannot borrow books for more than 45 days");
            if (readingCard.getBorrowBookList().size() == 5) throw new BadRequestException("Cannot borrow more than 5 books");
        } else if (Objects.equals(user.getRole(), RoleEnum.VIP_USER.name())) {
            if (dto.getNumberOfDays() > 60) throw new BadRequestException("Cannot borrow books for more than 60 days");
            if (readingCard.getBorrowBookList().size() == 8) throw new BadRequestException("Cannot borrow more than 8 books");
        }
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found"));
        if (!book.getIsAvailable()) {
            throw new BadRequestException("This book is unavailable");
        }
        BorrowBook borrowBook = BorrowBook.builder()
                .readingCard(readingCard)
                .book(book)
                .borrowDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(dto.getNumberOfDays()))
                .isLate(false)
                .updatedBy(email)
                .status(BorrowBookStatusEnum.NOT_RETURNED.ordinal())
                .build();
        return borrowBookRepository.save(borrowBook);
    }

    @Override
    public BorrowBook markAsOverdue(BorrowBook borrowing) {
        borrowing.setIsLate(true);
        borrowing.setOverDueDays((long) LocalDate.now().minusDays(borrowing.getReturnDate().toEpochDay()).getDayOfMonth());
        return borrowBookRepository.save(borrowing);
    }

    @Override
    public Page<BorrowBook> getBorrowBooksByCriteria(BorrowBookRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Pageable pageable = PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize(), Sort.by("id").descending());
        Specification<BorrowBook> spec = BorrowBookSpecification.byCriteria(dto);
        return borrowBookRepository.findAll(spec, pageable);
    }

    @Override
    public BorrowBook getBorrowingById(Long id) throws NotFoundException {
        return borrowBookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Borrow book not found"));
    }

    @Override
    public BorrowBook updateReturnedBorrowing(Long id) throws NotFoundException, MessagingException {
        BorrowBook borrowBook = getBorrowingById(id);
        borrowBook.setStatus(BorrowBookStatusEnum.RETURNED.ordinal());
        Book book = borrowBook.getBook();
        book.setIsAvailable(true);
        book = bookRepository.save(book);
        notificationService.notifySubscribers(book);
        return borrowBookRepository.save(borrowBook);
    }
}
