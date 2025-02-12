package com.vfedotov.notification_sender.details;

import com.vfedotov.core.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailDetails {

    private String recipient;
    private String msgBody;
    private List<FileDto> attachment;
}