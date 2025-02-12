package com.vfedotov.notification.dto;

import com.vfedotov.notification.validator.PhoneNumber;

import javax.validation.constraints.*;

public record CreateContactDto(
        @NotNull(message = "Notification group id must not be null!")
        Long notificationGroupId,

        @Email(message = "Invalid email format!")
        String email,

        @NotEmpty(message = "Name must not be null!")
        @Size(min = 3, max = 20, message = "Invalid name length!")
        String name,

        @PhoneNumber(message = "Invalid phone number")
        String phoneNumber,

        @NotEmpty(message = "Surname must not be null!")
        @Size(min = 3, max = 20, message = "Invalid surname length!")
        String surname
) {}
