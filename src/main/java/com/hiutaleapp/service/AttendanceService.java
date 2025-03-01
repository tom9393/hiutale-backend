package com.hiutaleapp.service;

import com.hiutaleapp.dto.AttendanceDTO;
import com.hiutaleapp.entity.Attendance;
import com.hiutaleapp.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<AttendanceDTO> getAllAttendances() {
        return attendanceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AttendanceDTO> getAttendanceById(Long id) {
        return attendanceRepository.findById(id).map(this::mapToDTO);
    }

    public List<AttendanceDTO> getAttendanceByUser(Long id) {
        return attendanceRepository.findByUser_UserId(id).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AttendanceDTO> getAttendanceByUserIdAndEventId(Long userId, Long eventId) {
        return attendanceRepository.findByUser_UserIdAndEvent_EventId(userId, eventId).map(this::mapToDTO);
    }

    public AttendanceDTO createAttendance(Attendance attendance) {
        return mapToDTO(attendanceRepository.save(attendance));
    }

    public AttendanceDTO updateAttendance(Long id, Attendance attendance) {
        attendance.setAttendeeId(id);
        return mapToDTO(attendanceRepository.save(attendance));
    }

    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    public AttendanceDTO mapToDTO(Attendance attendance) {
        return new AttendanceDTO(attendance);
    }

    public Attendance mapToEntity(AttendanceDTO attendanceDTO) {
        Attendance attendance = new Attendance();
        attendance.setCreatedAt(attendanceDTO.getRegistrationTime());
        return attendance;
    }

    public long countByEventId(Long eventId) {
        return attendanceRepository.countByEvent_EventId(eventId);
    }
}