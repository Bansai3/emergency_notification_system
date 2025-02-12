package com.vfedotov.notification.service;

import com.vfedotov.notification.dao.entity.NotificationGroup;
import com.vfedotov.notification.dao.repository.ContactRepository;
import com.vfedotov.notification.dao.repository.NotificationGroupRepository;
import com.vfedotov.notification.dto.CreateNotificationGroupDto;
import com.vfedotov.notification.dto.NotificationGroupDto;
import com.vfedotov.notification.mapper.NotificationGroupMapper;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class NotificationGroupService {

    @Autowired
    private NotificationGroupRepository notificationGroupRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private NotificationGroupMapper notificationGroupMapper;


    public void createNotificationGroup(CreateNotificationGroupDto notificationGroupDto) throws IOException, CsvException {
        NotificationGroup notificationGroup =
                notificationGroupMapper.fromCreateNotificationGroupDtotoNotificationGroup(notificationGroupDto);
        notificationGroupRepository.save(notificationGroup);
        contactRepository.saveAll(notificationGroup.getContacts());
    }

    public List<NotificationGroupDto> getUserNotificationGroups(String login) {
        List<NotificationGroup> groups = notificationGroupRepository.findNotificationGroupsByUserLogin(login);
        return groups.stream()
                .map(g -> notificationGroupMapper.fromNotificationGroupToNotificationGroupDto(g))
                .toList();
    }

    public NotificationGroupDto getNotificationGroup(Long notificationGroupId) {
        NotificationGroup notificationGroup = getNotificationGroupEntity(notificationGroupId);
        return notificationGroupMapper.fromNotificationGroupToNotificationGroupDto(notificationGroup);
    }

    public void deleteNotificationGroup(Long notificationGroupId) {
        getNotificationGroupEntity(notificationGroupId);
        notificationGroupRepository.deleteById(notificationGroupId);
    }

    private NotificationGroup getNotificationGroupEntity(Long notificationGroupId) {
        return notificationGroupRepository.findById(notificationGroupId).
                orElseThrow( () ->
                        new NoSuchElementException("Notification group with id " + notificationGroupId + " does not exist!")
                );
    }

}
