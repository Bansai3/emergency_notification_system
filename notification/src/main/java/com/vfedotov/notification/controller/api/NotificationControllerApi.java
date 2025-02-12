package com.vfedotov.notification.controller.api;

import com.vfedotov.core.dto.FileDto;
import com.vfedotov.notification.dto.CreateNotificationDto;
import com.vfedotov.notification.dto.NotificationDto;
import com.vfedotov.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class NotificationControllerApi {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("notification")
    public ResponseEntity<String> createNotification(
            @RequestParam("user_login") String userLogin,
            @ModelAttribute @Validated CreateNotificationDto notificationDto) {

        notificationDto.setLogin(userLogin);
        notificationDto.setCreationDate(LocalDateTime.now());
        notificationService.createNotification(notificationDto);

        return ResponseEntity.ok("Notification successfully created!");
    }

    @GetMapping
    public List<NotificationDto> getNotifications(
            @RequestParam("user_login") String userLogin) {
        return notificationService.getAllUserNotifications(userLogin);
    }

    @GetMapping("notification/{notification_id}")
    public NotificationDto getNotification(
            @PathVariable("notification_id") Long id) {
        return notificationService.getNotification(id);
    }

    @DeleteMapping("notification/{notification_id}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable("notification_id") Long id
    ) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notification successfully deleted!");
    }


    @GetMapping("notification/{notification_id}/files")
    public List<FileDto> getNotificationFiles(
            @PathVariable("notification_id") Long notificationId) {
        return notificationService.getNotificationFiles(notificationId);
    }
}
