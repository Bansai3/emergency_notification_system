package com.vfedotov.notification.service;

import com.vfedotov.core.dto.ContactDto;
import com.vfedotov.notification.dao.entity.Contact;
import com.vfedotov.notification.dao.entity.NotificationGroup;
import com.vfedotov.notification.dao.repository.ContactRepository;
import com.vfedotov.notification.dao.repository.NotificationGroupRepository;
import com.vfedotov.notification.dto.CreateContactDto;
import com.vfedotov.notification.mapper.ContactMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private NotificationGroupRepository notificationGroupRepository;

    @Autowired
    private ContactMapper contactMapper;

    public void createContact(CreateContactDto createContactDto) {
        Contact contact = contactMapper.fromCreateContactDtoToContact(createContactDto);

        NotificationGroup notificationGroup = contact.getNotificationGroup();
        notificationGroup.getContacts().add(contact);

        contactRepository.save(contact);
        notificationGroupRepository.save(notificationGroup);
    }

    public void deleteContact(Long contactId) {
        getContactEntity(contactId);
        contactRepository.deleteById(contactId);
    }

    public ContactDto getContact(Long contactId) {
        Contact contact = getContactEntity(contactId);
        return contactMapper.fromContactToContactDto(contact);
    }

    public List<ContactDto> getAllNotificationGroupContacts(Long notificationGroupId) {
        return contactRepository.findContactsByNotificationGroupId(notificationGroupId)
                .stream().map(c -> contactMapper.fromContactToContactDto(c)).toList();
    }

    private Contact getContactEntity(Long contactId) {
        return contactRepository.findById(contactId).
                orElseThrow( () ->
                        new NoSuchElementException("Contact with id " + contactId + " does not exist!")
                );
    }
}
