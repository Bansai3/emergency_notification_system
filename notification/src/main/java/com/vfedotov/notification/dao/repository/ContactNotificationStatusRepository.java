package com.vfedotov.notification.dao.repository;

import com.vfedotov.notification.dao.entity.ContactNotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactNotificationStatusRepository extends JpaRepository<ContactNotificationStatus, Long> {
    Optional<ContactNotificationStatus> findContactNotificationStatusByContactIdAndNotificationId(
            Long contactId, Long notificationId);
    List<ContactNotificationStatus> findContactNotificationStatusByContactId(Long contactId);
    List<ContactNotificationStatus> findContactNotificationStatusByNotificationId(Long notificationId);
}
