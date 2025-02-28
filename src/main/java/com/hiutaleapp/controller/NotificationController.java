package com.hiutaleapp.controller;

import com.hiutaleapp.dto.NotificationDTO;
import com.hiutaleapp.entity.Notification;
import com.hiutaleapp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public NotificationDTO createNotification(@RequestBody Notification notification) {
        return notificationService.createNotification(notification);
    }

//    @PutMapping("/update/{id}")
//    public NotificationDTO updateNotification(@PathVariable Long id, @RequestBody Notification notification) {
//        return notificationService.updateNotification(id, notification);
//    }

    @DeleteMapping("/delete/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }
}