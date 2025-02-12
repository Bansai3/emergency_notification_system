package com.vfedotov.notification.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactNotificationKey implements Serializable {
    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "notification_id")
    private Long notificationId;
}
