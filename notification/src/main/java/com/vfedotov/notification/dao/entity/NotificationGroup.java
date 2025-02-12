package com.vfedotov.notification.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications_groups")
public class NotificationGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ng_seq")
    @SequenceGenerator(name = "ng_seq", sequenceName = "ng_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "group_name", unique = true)
    private String groupName;

    @OneToMany(mappedBy = "notificationGroup", fetch = FetchType.LAZY)
    private List<Contact> contacts;
}
