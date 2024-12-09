package com.hust.project3.services.impl;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.notificationSubscription.NotificationSubscriptionRequestDTO;
import com.hust.project3.entities.Book;
import com.hust.project3.entities.NotificationSubscription;
import com.hust.project3.entities.User;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.BookRepository;
import com.hust.project3.repositories.NotificationSubscriptionRepository;
import com.hust.project3.repositories.UserRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.NotificationSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSubscriptionServiceImpl implements NotificationSubscriptionService {

    private final NotificationSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Page<NotificationSubscription> getSubscriptions(String jwt, PagingRequestDTO pagingRequestDTO) throws NotFoundException {
        Pageable pageable = PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize());
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return subscriptionRepository.findByUser(user, pageable);
    }

    @Override
    public NotificationSubscription subscribeToBook(NotificationSubscriptionRequestDTO dto, String jwt) throws NotFoundException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found"));

        // Check if subscription already exists
        if (subscriptionRepository.findByUserIdAndBookId(user.getId(), book.getId()).isPresent()) {
            throw new IllegalArgumentException("Already subscribed to this book");
        }

        NotificationSubscription subscription = NotificationSubscription.builder()
                .user(user)
                .book(book)
                .createdAt(LocalDateTime.now())
                .build();

        return subscriptionRepository.save(subscription);
    }

    @Override
    public NotificationSubscription unsubscribeFromBook(NotificationSubscriptionRequestDTO dto, String jwt) throws NotFoundException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        NotificationSubscription subscription = subscriptionRepository.findByUserIdAndBookId(user.getId(), dto.getBookId())
                .orElseThrow(() -> new NotFoundException("Subscription not found"));
        subscriptionRepository.delete(subscription);
        return subscription;
    }
}
