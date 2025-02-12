package com.vfedotov.notification.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationDto{

    @NotEmpty(message = "Notification group name must not be empty!")
    String notificationGroupName;

    @NotEmpty(message = "Text must not be null!")
    String text;

    List<MultipartFile> files;

    String login;

    LocalDateTime creationDate;
}
