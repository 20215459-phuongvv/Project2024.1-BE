package com.hust.project3.controllers;

import com.hust.project3.entities.Notification;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public List<Notification> getAllNotifications(@RequestHeader("Authorization") String jwt) throws NotFoundException {
        return notificationService.getAllNotifications(jwt);
    }

    @GetMapping("/unread")
    public List<Notification> getUnreadNotifications(@RequestHeader("Authorization") String jwt) throws NotFoundException {
        return notificationService.getUnreadNotifications(jwt);
    }

    @PutMapping("/{notificationId}/read")
    public String markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return "Notification marked as read";
    }
}

