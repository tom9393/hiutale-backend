package com.hiutaleapp.repository;

import com.hiutaleapp.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.user.userId = :userId AND n.displayAfter <= CURRENT_TIMESTAMP")
    List<Notification> findAllDisplayedNotifications(@Param("userId") Long userId);
}
