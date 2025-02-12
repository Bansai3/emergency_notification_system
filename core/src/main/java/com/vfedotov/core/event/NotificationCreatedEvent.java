package com.vfedotov.core.event;

import com.vfedotov.core.dto.ContactDto;
import com.vfedotov.core.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationCreatedEvent {
    Long notificationId;
    String senderLogin;
    byte[] text;
    List<FileDto> files;
    List<ContactDto> contacts;
}
