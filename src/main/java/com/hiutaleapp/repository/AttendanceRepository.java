package com.hiutaleapp.repository;

import com.hiutaleapp.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUser_UserIdAndEvent_EventId(Long userId, Long eventId);

    List<Attendance> findByUser_UserId(Long userId);

    long countByEvent_EventId(Long eventId);
}
