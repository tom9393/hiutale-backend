package com.hiutaleapp.dto;

import com.hiutaleapp.entity.FileEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FileDTO {

    private long id;
    private String filename;
    private long userId;
    private long eventId;
    private Date createdAt;
    private Date updatedAt;

    public FileDTO(FileEntity file) {
        this.id = file.getId();
        this.filename = file.getFilename();
        this.userId = file.getUser().getUserId();
        this.eventId = file.getEvent().getEventId();
        this.createdAt = file.getCreatedAt();
        this.updatedAt = file.getUpdatedAt();
    }
}
