package com.vfedotov.notification.service;

import com.vfedotov.core.event.NotificationCreatedEvent;
import com.vfedotov.notification.dao.entity.ContactNotificationStatus;
import com.vfedotov.notification.dao.entity.Status;
import com.vfedotov.notification.dao.repository.ContactNotificationStatusRepository;
import com.vfedotov.notification.dto.ContactNotificationStatusDto;
import com.vfedotov.notification.mapper.ContactMapper;
import com.vfedotov.notification.mapper.ContactNotificationStatusMapper;
import com.vfedotov.notification.mapper.FileMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class ContactNotificationStatusService {

    @Autowired
    private ContactNotificationStatusRepository contactNotificationStatusRepository;

    @Autowired
    private ContactNotificationStatusMapper contactNotificationStatusMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    @Qualifier("kafka-template")
    private KafkaTemplate<String, NotificationCreatedEvent> producer;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void resendNotifications(LocalDateTime dateBorder) {
        logger.info("Date border: " + dateBorder.toString());
        var dateValues = contactNotificationStatusRepository.findAll().stream().map(cns -> cns.getNotification().getCreationDate()).toList();
        contactNotificationStatusRepository.findAll()
                .stream()
                .filter(cns -> cns.getStatus().equals(Status.IN_PROCESS) &&
                        cns.getNotification().getCreationDate().isBefore(dateBorder))
                .forEach(cns -> {
                    NotificationCreatedEvent event = new NotificationCreatedEvent(
                            cns.getNotification().getId(),
                            cns.getNotification().getUser().getLogin(),
                            cns.getNotification().getText(),
                            cns.getNotification().getFiles().stream().map(f -> fileMapper.fromFileToFileDto(f)).toList(),
                            List.of(contactMapper.fromContactToContactDto(cns.getContact()))
                    );
                    logger.info("notification id: " + event.getNotificationId());
                    CompletableFuture<SendResult<String, NotificationCreatedEvent>> future =
                            producer.send("notifications-topic", event.getNotificationId().toString(), event);
                    future.whenComplete((result, exception) -> {
                        if (exception != null) {
                            logger.error("Failed to send notification!");
                        }
                    });
                });
    }

    public ContactNotificationStatusDto getContactNotificationStatus(
            Long contactId, Long notificationId) {
        ContactNotificationStatus contactNotificationStatus = contactNotificationStatusRepository
                .findContactNotificationStatusByContactIdAndNotificationId(contactId, notificationId)
                .orElseThrow(() -> new NoSuchElementException("Contact notification status does not exist!"));
        return contactNotificationStatusMapper.fromContactNotificationStatusToContactNotificationStatusDto(contactNotificationStatus);
    }

    public List<ContactNotificationStatusDto> getContactNotificationStatusByContactId(Long contactId) {
        return contactNotificationStatusRepository
                .findContactNotificationStatusByContactId(contactId)
                .stream().map(cns -> contactNotificationStatusMapper.fromContactNotificationStatusToContactNotificationStatusDto(cns))
                .toList();
    }

    public List<ContactNotificationStatusDto> getContactNotificationStatusByNotificationId(Long notificationId) {
        return contactNotificationStatusRepository
                .findContactNotificationStatusByNotificationId(notificationId)
                .stream().map(cns -> contactNotificationStatusMapper.fromContactNotificationStatusToContactNotificationStatusDto(cns))
                .toList();
    }
}
