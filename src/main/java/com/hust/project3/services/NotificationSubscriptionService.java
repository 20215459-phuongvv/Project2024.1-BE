package com.hust.project3.services;

import com.hust.project3.dtos.notificationSubscription.NotificationSubscriptionRequestDTO;
import com.hust.project3.entities.Book;
import com.hust.project3.entities.NotificationSubscription;
import com.hust.project3.exceptions.NotFoundException;

public interface NotificationSubscriptionService {
    NotificationSubscription subscribeToBook(NotificationSubscriptionRequestDTO dto, String jwt) throws NotFoundException;

    NotificationSubscription unsubscribeFromBook(NotificationSubscriptionRequestDTO dto, String jwt) throws NotFoundException;
}