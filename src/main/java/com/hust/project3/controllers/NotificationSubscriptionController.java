package com.hust.project3.controllers;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.ResultMeta;
import com.hust.project3.dtos.notificationSubscription.NotificationSubscriptionRequestDTO;
import com.hust.project3.entities.NotificationSubscription;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.NotificationSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationSubscriptionController {

    private final NotificationSubscriptionService subscriptionService;

    @GetMapping("/subscriptions")
    public Result getSubscriptions(@RequestHeader("Authorization") String jwt, PagingRequestDTO pagingRequestDTO) throws NotFoundException {
        Page<NotificationSubscription> page = subscriptionService.getSubscriptions(jwt, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @PostMapping("/subscribe")
    public Result subscribeToBook(@RequestHeader("Authorization") String jwt,
                                  @RequestBody @Valid NotificationSubscriptionRequestDTO dto) throws NotFoundException {
        return Result.ok(subscriptionService.subscribeToBook(dto, jwt));
    }

    @DeleteMapping("/unsubscribe")
    public Result unsubscribeFromBook(@RequestHeader("Authorization") String jwt,
                                      @RequestBody NotificationSubscriptionRequestDTO dto) throws NotFoundException {
        return Result.ok(subscriptionService.unsubscribeFromBook(dto, jwt));
    }
}
