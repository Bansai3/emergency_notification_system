package com.vfedotov.notification.dto;

import com.vfedotov.notification.dao.entity.Status;

public record ContactNotificationStatusDto(
        Long contactId,
        Long notificationId,
        Status status
) {}
