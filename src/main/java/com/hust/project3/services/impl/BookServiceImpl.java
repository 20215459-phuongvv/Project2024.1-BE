package com.hust.project3.services.impl;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.book.BookRequestDTO;
import com.hust.project3.dtos.book.BookResponseDTO;
import com.hust.project3.entities.Author;
import com.hust.project3.entities.Book;
import com.hust.project3.entities.Publisher;
import com.hust.project3.entities.User;
import com.hust.project3.enums.BookTypeEnum;
import com.hust.project3.enums.EntityStatusEnum;
import com.hust.project3.enums.RoleEnum;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.*;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.BookService;
import com.hust.project3.services.CloudinaryService;
import com.hust.project3.services.NotificationService;
import com.hust.project3.specification.BookSpecification;
import com.hust.project3.utils.SecurityUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NotificationService notificationService;
    private final NotificationSubscriptionRepository notificationSubscriptionRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Page<BookResponseDTO> getBooksByProperties(BookRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Pageable pageable = PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize(), Sort.by("id").descending());
        Optional<User> userOptional = userRepository.findByEmail(SecurityUtil.getUserEmail());
        dto.setIsVip(userOptional.isPresent() && Objects.equals(userOptional.get().getRole(), RoleEnum.VIP_USER.name()));
        Specification<Book> spec = BookSpecification.byCriteria(dto);
        Page<Book> page = bookRepository.findAll(spec, pageable);
        Page<BookResponseDTO> bookResponseDTOPage = page.map(book -> {
            Boolean isSubscribed = userOptional
                    .map(currentUser -> notificationSubscriptionRepository.existsByUserIdAndBookId(currentUser.getId(), book.getId()))
                    .orElse(null);
            return BookResponseDTO.builder()
                    .id(book.getId())
                    .author(book.getAuthor())
                    .publisher(book.getPublisher())
                    .title(book.getTitle())
                    .type(book.getType())
                    .isAvailable(book.getIsAvailable())
                    .status(book.getStatus())
                    .isSubscribe(isSubscribed)
                    .thumbnail(book.getThumbnail())
                    .build();
        });
        if (dto.getIsSubscribe() != null) {
            List<BookResponseDTO> filteredList = bookResponseDTOPage.stream()
                    .filter(bookResponseDTO -> bookResponseDTO.getIsSubscribe().equals(dto.getIsSubscribe()))
                    .collect(Collectors.toList());

            bookResponseDTOPage = new PageImpl<>(filteredList, pageable, filteredList.size());
        }

        return bookResponseDTOPage;
    }

    @Override
    public Book getBookById(Long id) throws NotFoundException {

        return bookRepository.findByIdAndType(id, BookTypeEnum.NORMAL.ordinal())
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    @Override
    public Book addBook(String jwt, BookRequestDTO dto) throws NotFoundException, IOException {
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author not found"));
        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new NotFoundException("Publisher not found"));
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(author)
                .publisher(publisher)
                .isAvailable(dto.getIsAvailable())
                .type(dto.getType())
                .status(EntityStatusEnum.ACTIVE.ordinal())
                .updatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt))
                .thumbnail(cloudinaryService.upload(dto.getThumbnail().getBytes(), dto.getThumbnail().getOriginalFilename(), "thumbnails"))
                .build();
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(String jwt, BookRequestDTO dto) throws NotFoundException, MessagingException, IOException {
        Book book = bookRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Book not found"));
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author not found"));
        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new NotFoundException("Publisher not found"));
        book.setTitle(dto.getTitle());
        if (!book.getIsAvailable() && dto.getIsAvailable()) {
            notificationService.notifySubscribers(book);
        }
        if (dto.getThumbnail() != null) {
            book.setThumbnail(cloudinaryService.upload(dto.getThumbnail().getBytes(), dto.getThumbnail().getOriginalFilename(), "thumbnails"));
        }
        book.setIsAvailable(dto.getIsAvailable());
        book.setUpdatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt));
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setType(dto.getType());
        book.setStatus(dto.getStatus());
        return bookRepository.save(book);
    }

    @Override
    public Page<BookResponseDTO> getVipBooksByProperties(BookRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Pageable pageable = PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize());
        Specification<Book> spec = BookSpecification.byCriteria(dto);
        Page<Book> page = bookRepository.findAll(spec, pageable);
        String email = SecurityUtil.getUserEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);
        return page.map(book -> {
            Boolean isSubscribed = userOptional
                    .map(user -> notificationSubscriptionRepository.existsByUserIdAndBookId(user.getId(), book.getId()))
                    .orElse(null);
            return BookResponseDTO.builder()
                    .id(book.getId())
                    .author(book.getAuthor())
                    .publisher(book.getPublisher())
                    .title(book.getTitle())
                    .type(book.getType())
                    .isAvailable(book.getIsAvailable())
                    .status(book.getStatus())
                    .isSubscribe(isSubscribed)
                    .thumbnail(book.getThumbnail())
                    .build();
        });
    }

    @Override
    public Book getVipBookById(Long id) throws NotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    @Override
    public List<Book> deleteBooks(String jwt, List<Long> idList) throws NotFoundException {
        List<Book> result = new ArrayList<>();
        for (long id : idList) {
            Book book = getVipBookById(id);
            book.setStatus(EntityStatusEnum.INACTIVE.ordinal());
            book.setUpdatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt));
            result.add(bookRepository.save(book));
        }
        return result;
    }
}
