package com.hiutaleapp.service;

import com.hiutaleapp.dto.EventAttendeeDTO;
import com.hiutaleapp.entity.EventAttendee;
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

    public EventAttendeeDTO createEventAttendee(EventAttendee eventAttendee) {
        return mapToDTO(eventAttendeeRepository.save(eventAttendee));
    }

    public EventAttendeeDTO updateEventAttendee(Long id, EventAttendee eventAttendee) {
        eventAttendee.setEventAttendeeId(id);
        return mapToDTO(eventAttendeeRepository.save(eventAttendee));
    }

    public void deleteEventAttendee(Long id) {
        eventAttendeeRepository.deleteById(id);
    }

    public EventAttendeeDTO mapToDTO(EventAttendee eventAttendee) {
        return new EventAttendeeDTO(eventAttendee);
    }

    public EventAttendee mapToEntity(EventAttendeeDTO eventAttendeeDTO) {
        EventAttendee eventAttendee = new EventAttendee();
        eventAttendee.setEventAttendeeId(eventAttendeeDTO.getEventAttendeeId());
        eventAttendee.getEvent().setEventId(eventAttendeeDTO.getEventId());
        eventAttendee.getUser().setUserId(eventAttendeeDTO.getUserId());
        eventAttendee.setStatus(eventAttendeeDTO.getStatus());
        eventAttendee.setCreatedAt(eventAttendeeDTO.getRegistrationTime());
        return eventAttendee;
    }
}