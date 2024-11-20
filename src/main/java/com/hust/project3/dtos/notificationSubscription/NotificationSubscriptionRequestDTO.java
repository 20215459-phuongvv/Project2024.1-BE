package com.hust.project3.dtos.notificationSubscription;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationSubscriptionRequestDTO {
    @NotNull
    private Long bookId;
}
