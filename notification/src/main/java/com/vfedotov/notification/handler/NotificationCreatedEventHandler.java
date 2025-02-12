package com.vfedotov.notification.handler;

import com.vfedotov.core.event.EmailNotificationResultReceivedEvent;
import com.vfedotov.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "notifications-status-topic")
public class NotificationCreatedEventHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NotificationService notificationService;


    @KafkaHandler
    public void handle(EmailNotificationResultReceivedEvent event) {
        logger.info("Received notification status event");
        notificationService.changeContactsNotificationStatus(
                event.getNotificationId(),
                event.getContactId());
    }
}
