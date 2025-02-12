package com.vfedotov.notification.dao.repository;

import com.vfedotov.notification.dao.entity.NotificationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationGroupRepository extends JpaRepository<NotificationGroup, Long> {

    List<NotificationGroup> findNotificationGroupsByUserLogin(String userLogin);

    Optional<NotificationGroup> findNotificationGroupByUserLoginAndGroupName(String userLogin, String groupName);
}
