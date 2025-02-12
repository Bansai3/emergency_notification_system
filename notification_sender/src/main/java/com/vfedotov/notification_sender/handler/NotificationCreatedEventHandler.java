package com.vfedotov.notification_sender.handler;

import com.vfedotov.core.event.EmailNotificationResultReceivedEvent;
import com.vfedotov.core.event.NotificationCreatedEvent;
import com.vfedotov.notification_sender.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "notifications-topic")
public class NotificationCreatedEventHandler {

    @Autowired
    private EmailService emailService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @KafkaHandler
    public void handle(NotificationCreatedEvent event) {
        logger.info("Received notification request");
        emailService.sendNotification(event);
    }
}
