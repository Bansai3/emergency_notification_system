package com.vfedotov.notification.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationGroupDto {
    @NotEmpty(message = "Group name must not be null or empty!")
    @Size(min = 3, max = 20, message = "Invalid length of group name!")
    String groupName;

    @NotNull(message = "Contacts file must not be null!")
    MultipartFile contacts_file;

    String userLogin;
}
