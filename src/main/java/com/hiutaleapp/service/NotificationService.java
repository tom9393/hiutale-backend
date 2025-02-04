package com.hiutaleapp.service;

import com.hiutaleapp.dto.NotificationDTO;
import com.hiutaleapp.entity.Notification;
import com.hiutaleapp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<NotificationDTO> getNotificationById(Long id) {
        return notificationRepository.findById(id).map(this::mapToDTO);
    }

    public NotificationDTO createNotification(Notification notification) {
        return mapToDTO(notificationRepository.save(notification));
    }

    public NotificationDTO updateNotification(Long id, Notification notification) {
        notification.setNotificationId(id);
        return mapToDTO(notificationRepository.save(notification));
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public NotificationDTO mapToDTO(Notification notification) {
        return new NotificationDTO(notification);
    }

    public Notification mapToEntity(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setNotificationId(notificationDTO.getNotificationId());
        notification.getUser().setUserId(notificationDTO.getUserId());
        notification.setMessage(notificationDTO.getMessage());
        notification.setReadStatus(notificationDTO.getReadStatus());
        notification.setCreatedAt(notificationDTO.getCreatedAt());
        return notification;
    }
}