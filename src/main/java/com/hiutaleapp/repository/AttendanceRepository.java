package com.hiutaleapp.repository;

import com.hiutaleapp.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUser_UserIdAndEvent_EventId(Long userId, Long eventId);

    long countByEvent_EventId(Long eventId);
}
