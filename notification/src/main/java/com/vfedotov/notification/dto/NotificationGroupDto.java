package com.vfedotov.notification.dto;

import com.vfedotov.core.dto.ContactDto;

import java.util.Set;

public record NotificationGroupDto (
        Long id,
        String groupName,
        Set<ContactDto> contacts
) {}
