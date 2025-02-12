package com.vfedotov.notification.mapper;

import com.vfedotov.core.dto.ContactDto;
import com.vfedotov.notification.dao.entity.Contact;
import com.vfedotov.notification.dao.entity.NotificationGroup;
import com.vfedotov.notification.dao.repository.NotificationGroupRepository;
import com.vfedotov.notification.dto.CreateContactDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class ContactMapper {

    @Autowired
    private NotificationGroupRepository notificationGroupRepository;

    public ContactDto fromContactToContactDto(Contact contact) {
        return new ContactDto(
                contact.getId(),
                contact.getName(),
                contact.getSurname(),
                contact.getEmail(),
                contact.getPhoneNumber(),
                contact.getNotificationGroup().getId()
        );
    }

    public Contact fromCreateContactDtoToContact(CreateContactDto contactDto) {
        NotificationGroup notificationGroup = getNotificationGroupEntity(contactDto.notificationGroupId());
        return new Contact(
                null,
                notificationGroup,
                contactDto.name(),
                contactDto.surname(),
                contactDto.email(),
                contactDto.phoneNumber()
        );
    }

    private NotificationGroup getNotificationGroupEntity(Long notificationGroupId) {
        return notificationGroupRepository.findById(notificationGroupId).
                orElseThrow( () ->
                        new NoSuchElementException("Notification group with id " + notificationGroupId + " does not exist!")
                );
    }

}
