package com.vfedotov.notification.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(
        @NotEmpty(message = "Name must not be empty or null!")
        @Size(min = 3, max = 20, message = "Incorrect name length!")
        String name,
        @NotEmpty(message = "Surname must not be empty or null!")
        @Size(min = 3, max = 20, message = "Incorrect surname length!")
        String surname,

        @NotEmpty(message = "Login must not be empty or null!")
        @Size(min = 3, max = 30, message = "Incorrect login length!")
        String login,

        @NotEmpty(message = "Password must not be empty or null!")
        @Size(min = 3, max = 30, message = "Incorrect password length!")
        String password
) {}
