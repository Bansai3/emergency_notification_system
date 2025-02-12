package com.vfedotov.notification.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="notifications")
public class Notification implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "n_seq")
    @SequenceGenerator(name = "n_seq", sequenceName = "n_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date")
    private LocalDateTime creationDate;

    @Lob
    @Column(name = "message")
    private byte[] text;

    @ElementCollection
    @CollectionTable(name = "notification_files", joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "files")
    private List<FileEntity> files;

    @ManyToOne
    @JoinColumn(name = "notification_group_id")
    private NotificationGroup notificationGroup;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
