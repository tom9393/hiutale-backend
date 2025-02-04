package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Notification;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NotificationDTO {
    private Long notificationId;
    private Long userId;
    private String message;
    private Boolean readStatus;
    private Date createdAt;

    public NotificationDTO(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.userId = notification.getUser().getUserId();
        this.message = notification.getMessage();
        this.readStatus = notification.getReadStatus();
        this.createdAt = notification.getCreatedAt();
    }
}