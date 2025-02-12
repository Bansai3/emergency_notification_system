package com.vfedotov.notification.service;

import com.vfedotov.core.dto.FileDto;
import com.vfedotov.core.event.NotificationCreatedEvent;
import com.vfedotov.notification.dao.entity.*;
import com.vfedotov.notification.dao.repository.ContactNotificationStatusRepository;
import com.vfedotov.notification.dao.repository.NotificationRepository;
import com.vfedotov.notification.dto.CreateNotificationDto;
import com.vfedotov.notification.dto.NotificationDto;
import com.vfedotov.notification.mapper.ContactMapper;
import com.vfedotov.notification.mapper.FileMapper;
import com.vfedotov.notification.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class NotificationService {

    @Autowired
    @Qualifier("kafka-template")
    private KafkaTemplate<String, NotificationCreatedEvent> producer;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ContactNotificationStatusRepository contactNotificationStatusRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private ContactMapper contactMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void createNotification(CreateNotificationDto dto) {

        Notification notification = notificationMapper.fromCreatNotificationDtoToNotification(dto);
        notificationRepository.save(notification);

        List<Contact> contacts = notification.getNotificationGroup().getContacts();

        Status status = Status.IN_PROCESS;
        List<ContactNotificationStatus> contactNotificationsStatuses = contacts.stream()
                .map(c -> new ContactNotificationStatus(
                            new ContactNotificationKey(c.getId(), notification.getId()),
                            c,
                            notification,
                            status)
                )
                .toList();

        contactNotificationStatusRepository.saveAll(contactNotificationsStatuses);

        NotificationCreatedEvent event = new NotificationCreatedEvent(
                notification.getId(),
                dto.getLogin(),
                notification.getText(),
                notification.getFiles().stream().map(f -> fileMapper.fromFileToFileDto(f)).toList(),
                notification.getNotificationGroup().getContacts()
                        .stream()
                        .map(contact -> contactMapper.fromContactToContactDto(contact))
                        .toList()
        );

        sendNotification(event, notification.getId());
    }

    public void sendNotification(NotificationCreatedEvent event, Long notificationId) {
        logger.info("notification id: " + notificationId);
        CompletableFuture<SendResult<String, NotificationCreatedEvent>> future =
                producer.send("notifications-topic", notificationId.toString(), event);
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                logger.error("Failed to send notification!");
            }
        });
    }

    public void changeContactsNotificationStatus(Long notificationId, Long contactId) {
        Optional<ContactNotificationStatus> contactNotificationStatus =
                contactNotificationStatusRepository.findContactNotificationStatusByContactIdAndNotificationId(
                        contactId,
                        notificationId
                );
        if(contactNotificationStatus.isPresent()) {
            contactNotificationStatus.get().setStatus(Status.SEND);
        } else {
            logger.error("Contact notification status with notification id = " + notificationId
            + " and contact id = " + contactId + " does not exist!");
        }
    }

    public NotificationDto getNotification(Long notificationId) {
        Notification notification = getNotificationEntity(notificationId);
        return notificationMapper.fromNotificationToNotificationDto(notification);
    }

    public List<NotificationDto> getAllUserNotifications(String userLogin) {
        return notificationRepository.findNotificationsByUserLogin(userLogin)
                .stream()
                .map(notification ->
                        notificationMapper.fromNotificationToNotificationDto(notification)
                ).toList();

    }

    public void deleteNotification(Long notificationId) {
        getNotificationEntity(notificationId);
        notificationRepository.deleteById(notificationId);
    }

    public List<FileDto> getNotificationFiles(Long notificationId) {
        Notification notification = getNotificationEntity(notificationId);
        return notification.getFiles()
                .stream()
                .map(f -> fileMapper.fromFileToFileDto(f))
                .toList();
    }

    private Notification getNotificationEntity(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow( () ->
                        new NoSuchElementException("Notification with id " + notificationId + " does not exist!")
                );
    }
}
