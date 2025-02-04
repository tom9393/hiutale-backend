package com.hiutaleapp.service;

import com.hiutaleapp.dto.EventDTO;
import com.hiutaleapp.entity.Event;
import com.hiutaleapp.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EventDTO> getEventById(Long id) {
        return eventRepository.findById(id).map(this::mapToDTO);
    }

    public EventDTO createEvent(Event event) {
        return mapToDTO(eventRepository.save(event));
    }

    public EventDTO updateEvent(Long id, Event event) {
        event.setEventId(id);
        return mapToDTO(eventRepository.save(event));
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public EventDTO mapToDTO(Event event) {
        return new EventDTO(event);
    }

    public Event mapToEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setEventId(eventDTO.getEventId());
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setCapacity(eventDTO.getCapacity());
        event.setStartTime(eventDTO.getStartTime());
        event.setEndTime(eventDTO.getEndTime());
        event.setStatus(eventDTO.getStatus());
        event.setCreatedAt(eventDTO.getCreatedAt());
        event.setUpdatedAt(eventDTO.getUpdatedAt());
        return event;
    }
}