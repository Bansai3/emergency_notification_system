package com.vfedotov.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationResultReceivedEvent {
    Long notificationId;
    Long contactId;
}
