package com.vfedotov.core.dto;

public record ContactDto(
        Long id,
        String name,
        String surname,
        String email,
        String phoneNumber,
        Long notificationGroupId
) {}
