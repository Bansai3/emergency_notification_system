package com.vfedotov.notification.dto;

import com.vfedotov.core.dto.FileDto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public record NotificationDto(
        Long id,
        byte[] text,
        Set<FileDto> files,
        Long notificationGroupId,
        LocalDateTime creationDate
) {}
