package com.vfedotov.notification.mapper;
import com.vfedotov.core.dto.FileDto;
import com.vfedotov.notification.dao.entity.FileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public FileEntity fromFileDtoToFileEntity(FileDto fileDto) {
        return new FileEntity (
                fileDto.name(),
                fileDto.file()
        );
    }

    public FileDto fromFileToFileDto(FileEntity file) {
        return new FileDto (
                file.getFile(),
                file.getName()
        );
    }
}
