package com.vfedotov.notification.controller.api;

import com.vfedotov.notification.dto.CreateNotificationGroupDto;
import com.vfedotov.notification.dto.NotificationGroupDto;
import com.vfedotov.notification.service.NotificationGroupService;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/notification_groups")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class NotificationGroupControllerApi {

    @Autowired
    private NotificationGroupService notificationGroupService;

    @PostMapping("notification_group")
    public ResponseEntity<String> createNotificationGroup(
            @RequestParam("user_login") String userLogin,
            @ModelAttribute @Validated CreateNotificationGroupDto dto) throws IOException, CsvException {

        dto.setUserLogin(userLogin);
        notificationGroupService.createNotificationGroup(dto);
        return ResponseEntity.ok("Notification group successfully created!");
    }

    @GetMapping
    public List<NotificationGroupDto> getNotificationGroups(
            @RequestParam("user_login") String userLogin) {
        return notificationGroupService.getUserNotificationGroups(userLogin);
    }

    @GetMapping("notification_group/{notification_group_id}")
    public NotificationGroupDto getNotificationGroup(
            @PathVariable("notification_group_id") Long id
    ) {
        return notificationGroupService.getNotificationGroup(id);
    }


    @DeleteMapping("notification_group/{notification_group_id}")
    public ResponseEntity<String> deleteNotificationGroup(
            @PathVariable("notification_group_id") Long id) {
        notificationGroupService.deleteNotificationGroup(id);
        return ResponseEntity.ok("Notification group successfully deleted!");
    }
}
