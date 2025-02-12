package com.vfedotov.notification.dao.repository;

import com.vfedotov.notification.dao.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findContactsByNotificationGroupId(Long notificationGroupId);
}
