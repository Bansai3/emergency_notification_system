package com.vfedotov.notification.controller.api;

import com.vfedotov.notification.dto.ContactNotificationStatusDto;
import com.vfedotov.notification.service.ContactNotificationStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contact_notification_statuses")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class ContactNotificationStatusController {

    @Autowired
    private ContactNotificationStatusService contactNotificationStatusService;

    @GetMapping("contact_notification_status/{contact_id}/{notification_id}")
    public ContactNotificationStatusDto getContactNotificationStatus(
            @PathVariable("contact_id") Long contactId,
            @PathVariable("notification_id") Long notificationId) {
        return contactNotificationStatusService.getContactNotificationStatus(
                contactId, notificationId);
    }

    @GetMapping("contact_notification_status/{contact_id}")
    public List<ContactNotificationStatusDto> getContactNotificationStatusesByContactId(
            @PathVariable("contact_id") Long contactId) {
        return contactNotificationStatusService.getContactNotificationStatusByContactId(contactId);
    }

    @GetMapping("contact_notification_status/{notification_id}")
    public List<ContactNotificationStatusDto> getContactNotificationStatusesByNotificationId(
            @PathVariable("notification_id") Long notificationId) {
        return contactNotificationStatusService.getContactNotificationStatusByNotificationId(notificationId);
    }
}
