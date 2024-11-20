package com.hust.project3.repositories;

import com.hust.project3.entities.NotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscription, Long> {
    Optional<NotificationSubscription> findByUserIdAndBookId(Long userId, Long bookId);

    List<NotificationSubscription> findAllByBookId(Long id);
}
