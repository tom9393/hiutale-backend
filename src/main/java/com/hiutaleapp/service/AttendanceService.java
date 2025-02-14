package com.hiutaleapp.service;

import com.hiutaleapp.dto.EventAttendeeDTO;
import com.hiutaleapp.entity.Attendance;
import com.hiutaleapp.repository.EventAttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventAttendeeService {
    @Autowired
    private EventAttendeeRepository eventAttendeeRepository;

    public List<EventAttendeeDTO> getAllEventAttendees() {
        return eventAttendeeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EventAttendeeDTO> getEventAttendeeById(Long id) {
        return eventAttendeeRepository.findById(id).map(this::mapToDTO);
    }

    public EventAttendeeDTO createEventAttendee(Attendance attendance) {
        return mapToDTO(eventAttendeeRepository.save(attendance));
    }

    public EventAttendeeDTO updateEventAttendee(Long id, Attendance attendance) {
        attendance.setEventAttendeeId(id);
        return mapToDTO(eventAttendeeRepository.save(attendance));
    }

    public void deleteEventAttendee(Long id) {
        eventAttendeeRepository.deleteById(id);
    }

    public EventAttendeeDTO mapToDTO(Attendance attendance) {
        return new EventAttendeeDTO(attendance);
    }

    public Attendance mapToEntity(EventAttendeeDTO eventAttendeeDTO) {
        Attendance attendance = new Attendance();
        attendance.setEventAttendeeId(eventAttendeeDTO.getEventAttendeeId());
        attendance.getEvent().setEventId(eventAttendeeDTO.getEventId());
        attendance.getUser().setUserId(eventAttendeeDTO.getUserId());
        attendance.setStatus(eventAttendeeDTO.getStatus());
        attendance.setCreatedAt(eventAttendeeDTO.getRegistrationTime());
        return attendance;
    }
}