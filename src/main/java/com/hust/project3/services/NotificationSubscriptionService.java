package com.hust.project3.services;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.notificationSubscription.NotificationSubscriptionRequestDTO;
import com.hust.project3.entities.Book;
import com.hust.project3.entities.NotificationSubscription;
import com.hust.project3.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

public interface NotificationSubscriptionService {
    Page<NotificationSubscription> getSubscriptions(String jwt, PagingRequestDTO pagingRequestDTO) throws NotFoundException;
    NotificationSubscription subscribeToBook(NotificationSubscriptionRequestDTO dto, String jwt) throws NotFoundException;

    NotificationSubscription unsubscribeFromBook(NotificationSubscriptionRequestDTO dto, String jwt) throws NotFoundException;
}