package com.vfedotov.notification.mapper;

import com.vfedotov.core.dto.ContactDto;
import com.vfedotov.notification.dao.entity.Contact;
import com.vfedotov.notification.dao.entity.NotificationGroup;
import com.vfedotov.notification.dao.entity.User;
import com.vfedotov.notification.dao.repository.NotificationGroupRepository;
import com.vfedotov.notification.dao.repository.UserRepository;
import com.vfedotov.notification.dto.CreateNotificationGroupDto;
import com.vfedotov.notification.dto.NotificationGroupDto;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NotificationGroupMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationGroupRepository notificationGroupRepository;

    @Autowired
    private ContactMapper contactMapper;

    private final String[] columnsTitlesValues = {"name", "surname", "email", "phone_number"};

    public NotificationGroup fromCreateNotificationGroupDtotoNotificationGroup(
            CreateNotificationGroupDto createNotificationGroupDto) throws IOException, CsvException {
        User user = getUserEntity(createNotificationGroupDto.getUserLogin());
        String name = createNotificationGroupDto.getGroupName();
        checkSimilarGroupNames(name, user.getLogin());
        NotificationGroup notificationGroup = new NotificationGroup(null, user, name, new ArrayList<>());
        List<Contact> contacts = parseContacts(
                createNotificationGroupDto.getContacts_file(),
                notificationGroup
        );
        notificationGroup.setContacts(contacts);

        return notificationGroup;
    }

    public NotificationGroupDto fromNotificationGroupToNotificationGroupDto(NotificationGroup notificationGroup) {
        Set<ContactDto> contacts = notificationGroup.getContacts().stream()
                .map(c -> contactMapper.fromContactToContactDto(c))
                .collect(Collectors.toSet());
        return new NotificationGroupDto (
                notificationGroup.getId(),
                notificationGroup.getGroupName(),
                contacts
        );
    }

    private List<Contact> parseContacts(MultipartFile file, NotificationGroup notificationGroup) throws IOException, CsvException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVReader csvReader = new CSVReader(reader)) {

             String[] columnsTitles = parseRaw(csvReader.readNext()[0]);
             checkColumnsNames(columnsTitles);

             List<String[]> lines = csvReader.readAll();
             List<Contact> contacts = new ArrayList<>();
            for (String[] line : lines) {
                String[] columns = parseRaw(line[0]);
                Contact contact = new Contact(
                        null,
                        notificationGroup,
                        columns[0],
                        columns[1],
                        columns[2],
                        columns[3]
                );
                contacts.add(contact);
            }
             return contacts;
        }
    }

    private String[] parseRaw(String raw) {
        return raw.split(";");
    }


    private void checkColumnsNames(String[] titles) {
        if (titles.length != columnsTitlesValues.length) {
            throw new InvalidParameterException("Invalid columns titles!");
        }
        for (int i = 0; i < titles.length; i++) {
            if (!columnsTitlesValues[i].equals(titles[i])) {
                throw new InvalidParameterException("Invalid columns titles!");
            }
        }
    }

    private void checkSimilarGroupNames(String newNotificationGroupName, String userLogin) {
        Optional<NotificationGroup> notificationGroup = notificationGroupRepository
                .findNotificationGroupByUserLoginAndGroupName(userLogin, newNotificationGroupName);
        if (notificationGroup.isPresent()) {
            throw new InvalidParameterException(
                    "Notification group with name "
                    + newNotificationGroupName
                    + " already exists!"
            );
        }
    }


    private User getUserEntity(String login) {
        return userRepository.findUserByLogin(login).
                orElseThrow(() -> new IllegalArgumentException("Invalid login!"));
    }
}
