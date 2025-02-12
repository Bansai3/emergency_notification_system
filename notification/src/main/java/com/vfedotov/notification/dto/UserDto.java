package com.vfedotov.notification.dto;

import com.vfedotov.notification.dao.entity.Role;

public record UserDto (
        Long id,
        String name,
        String surname,
        String login,
        String password,
        Role role
) {}
