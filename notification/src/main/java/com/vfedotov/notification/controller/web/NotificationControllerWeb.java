package com.vfedotov.notification.controller.web;

import com.opencsv.exceptions.CsvException;
import com.vfedotov.notification.dto.CreateNotificationDto;
import com.vfedotov.notification.dto.CreateNotificationGroupDto;
import com.vfedotov.notification.dto.NotificationGroupDto;
import com.vfedotov.notification.service.NotificationGroupService;
import com.vfedotov.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationControllerWeb {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationGroupService notificationGroupService;

    @PostMapping("notification")
    public ModelAndView createNotification(
            ModelAndView modelAndView,
            @AuthenticationPrincipal UserDetails user,
            @ModelAttribute @Validated CreateNotificationDto notificationDto,
            BindingResult bindingResult) {

        modelAndView.setViewName("notification_page");

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            return modelAndView;
        }
        notificationDto.setLogin(user.getUsername());
        notificationDto.setCreationDate(LocalDateTime.now());
        notificationService.createNotification(notificationDto);
        modelAndView.addObject("operation_status", "Success!");
        return modelAndView;
    }


    @PostMapping("notification_group")
    public ModelAndView createNotificationGroup(
            ModelAndView modelAndView,
            @AuthenticationPrincipal UserDetails user,
            @ModelAttribute @Validated CreateNotificationGroupDto dto,
            BindingResult bindingResult) throws IOException, CsvException {

        modelAndView.setViewName("notification_group_page");

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            return modelAndView;
        }
        dto.setUserLogin(user.getUsername());
        notificationGroupService.createNotificationGroup(dto);
        modelAndView.addObject("operation_status", "Success!");

        return modelAndView;
    }


    @GetMapping("notification_groups")
    public ModelAndView getAllNotificationGroups(
            ModelAndView modelAndView,
            @AuthenticationPrincipal UserDetails user) {
        List<NotificationGroupDto> notificationGroups = notificationGroupService.getUserNotificationGroups(user.getUsername());
        modelAndView.setViewName("groups_page");
        modelAndView.addObject("groups", notificationGroups);
        return modelAndView;
    }
}
