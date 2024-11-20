package com.hust.project3.services.impl;

import com.hust.project3.entities.Book;
import com.hust.project3.entities.Notification;
import com.hust.project3.entities.NotificationSubscription;
import com.hust.project3.entities.User;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.NotificationRepository;
import com.hust.project3.repositories.NotificationSubscriptionRepository;
import com.hust.project3.repositories.UserRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.EmailService;
import com.hust.project3.services.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    @Override
    public void createNotification(Long userId, String message) {
        Notification notification = Notification.builder()
                .user(User.builder().id(userId).build())
                .message(message)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUnreadNotifications(String jwt) throws NotFoundException {
        User user = userRepository.findByEmail(jwtTokenProvider.getEmailFromJwtToken(jwt))
                .orElseThrow(() -> new NotFoundException("User not found"));
        return notificationRepository.findAllByUserIdAndIsReadFalse(user.getId());
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void notifySubscribers(Book book) throws MessagingException {
        if (book.getIsAvailable()) {
            List<NotificationSubscription> subscriptions = subscriptionRepository.findAllByBookId(book.getId());
            for (NotificationSubscription subscription : subscriptions) {
                String message = "The book '" + book.getTitle() + "' is now available!";
                createNotification(subscription.getUser().getId(), message);
                Context context = new Context();
                context.setVariable("userName", subscription.getUser().getName());
                context.setVariable("bookName", subscription.getBook().getTitle());
                context.setVariable("bookLink", null);

                emailService.sendEmailWithHtmlTemplate(subscription.getUser().getEmail(),
                        message,
                        "notification-email",
                        context);
            }
        }
    }
}
