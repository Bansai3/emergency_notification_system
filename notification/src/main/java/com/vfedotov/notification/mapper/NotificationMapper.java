package com.vfedotov.notification.mapper;

import com.vfedotov.core.dto.FileDto;
import com.vfedotov.notification.dao.entity.FileEntity;
import com.vfedotov.notification.dao.entity.Notification;
import com.vfedotov.notification.dao.entity.NotificationGroup;
import com.vfedotov.notification.dao.entity.User;
import com.vfedotov.notification.dao.repository.NotificationGroupRepository;
import com.vfedotov.notification.dao.repository.UserRepository;
import com.vfedotov.notification.dto.CreateNotificationDto;
import com.vfedotov.notification.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationGroupRepository notificationGroupRepository;

    @Autowired
    private FileMapper fileMapper;


    public NotificationDto fromNotificationToNotificationDto(Notification notification) {
        Set<FileDto> files = notification.getFiles().stream()
                .map(f -> fileMapper.fromFileToFileDto(f))
                .collect(Collectors.toSet());
        return new NotificationDto (
                notification.getId(),
                notification.getText(),
                files,
                notification.getNotificationGroup().getId(),
                notification.getCreationDate()
        );
    }

    public Notification fromCreatNotificationDtoToNotification(CreateNotificationDto createNotificationDto) {
        User user = getUserEntity(createNotificationDto.getLogin());
        byte[] text = createNotificationDto.getText().getBytes();
        List<FileEntity> files = getFiles(createNotificationDto.getFiles());
        NotificationGroup notificationGroup =
                getNotificationGroupEntity(
                        createNotificationDto.getLogin(),
                        createNotificationDto.getNotificationGroupName()
                );
        LocalDateTime date = createNotificationDto.getCreationDate();

        return new Notification(null, user, date, text, files, notificationGroup);
    }


    private User getUserEntity(String login) {
        return userRepository.findUserByLogin(login).
                orElseThrow(() -> new IllegalArgumentException("Invalid login!"));
    }

    private List<FileEntity> getFiles(List<MultipartFile> files) {
        if (files.size() == 1 && files.get(0).isEmpty()) return new ArrayList<>();
        return files.stream().map(f -> {
            try {
                return new FileEntity(f.getOriginalFilename(), f.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private NotificationGroup getNotificationGroupEntity(String userLogin, String notificationGroupName) {
        return notificationGroupRepository.findNotificationGroupByUserLoginAndGroupName(userLogin, notificationGroupName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group name!"));
    }
}
