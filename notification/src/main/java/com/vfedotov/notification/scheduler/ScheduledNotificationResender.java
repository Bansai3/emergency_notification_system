package com.vfedotov.notification.scheduler;

import com.vfedotov.notification.service.ContactNotificationStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class ScheduledNotificationResender {
    private static final Logger log = LoggerFactory.getLogger(ScheduledNotificationResender.class);

    @Autowired
    private ContactNotificationStatusService contactNotificationStatusService;


    @Scheduled(fixedRate = 40000)
    public void resendNotification() {
        log.info("resend notifications...");
        LocalDateTime dateBorder = LocalDateTime.now().minusMinutes(5);
        contactNotificationStatusService.resendNotifications(dateBorder);
    }
}
