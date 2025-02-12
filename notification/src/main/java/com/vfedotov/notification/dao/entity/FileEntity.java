package com.vfedotov.notification.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FileEntity implements Serializable {
    private String name;

    @Lob
    private byte[] file;
}
