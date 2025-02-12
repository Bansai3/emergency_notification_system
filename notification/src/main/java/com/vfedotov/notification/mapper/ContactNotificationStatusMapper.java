package com.vfedotov.notification.mapper;

import com.vfedotov.notification.dao.entity.ContactNotificationStatus;
import com.vfedotov.notification.dto.ContactNotificationStatusDto;
import org.springframework.stereotype.Component;

@Component
public class ContactNotificationStatusMapper {

   public ContactNotificationStatusDto fromContactNotificationStatusToContactNotificationStatusDto(
           ContactNotificationStatus contactNotificationStatus) {
       return new ContactNotificationStatusDto(
               contactNotificationStatus.getId().getContactId(),
               contactNotificationStatus.getId().getNotificationId(),
               contactNotificationStatus.getStatus());
   }
}
