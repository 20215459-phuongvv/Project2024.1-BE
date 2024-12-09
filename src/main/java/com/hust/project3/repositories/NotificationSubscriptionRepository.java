package com.hust.project3.repositories;

import com.hust.project3.entities.NotificationSubscription;
import com.hust.project3.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscription, Long> {
    Optional<NotificationSubscription> findByUserIdAndBookId(Long userId, Long bookId);

    List<NotificationSubscription> findAllByBookId(Long id);

    Page<NotificationSubscription> findByUser(User user, Pageable pageable);
}
