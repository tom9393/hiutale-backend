package com.hiutaleapp.repository;

import com.hiutaleapp.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAttendeeRepository extends JpaRepository<Attendance, Long> {}
