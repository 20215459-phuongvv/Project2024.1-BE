package com.hust.project3.repositories;

import com.hust.project3.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserIdAndIsReadFalse(Long userId);
}
