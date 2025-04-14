package com.hiutaleapp.service;

import com.hiutaleapp.dto.EventDTO;
import com.hiutaleapp.entity.*;
import com.hiutaleapp.repository.EventRepository;
import com.hiutaleapp.util.errors.DataViolationException;
import com.hiutaleapp.util.errors.DatabaseConnectionException;
import com.hiutaleapp.util.forms.EventForm;
import com.hiutaleapp.util.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    private static EventDTO objectToDTO(Object[] result) {
        Event event = (Event) result[0];
        Long attendanceCount = (Long) result[1];
        Long favouriteCount = (Long) result[2];

        EventDTO eventDTO = new EventDTO(event);
        eventDTO.setAttendanceCount(attendanceCount);
        eventDTO.setFavouriteCount(favouriteCount);
        return eventDTO;
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getAllEventsWithCounts() {
        return eventRepository.findAllEventsWithAttendanceAndFavouritesCount()
                .stream()
                .map(EventService::objectToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EventDTO> getEventById(Long id) {
        return eventRepository.findById(id).map(this::mapToDTO);
    }

    public EventDTO getEventWithCounts(Long eventId) {
        List<EventDTO> eventDTOs = eventRepository.findEventWithAttendanceAndFavouritesCount(eventId)
                .stream()
                .map(EventService::objectToDTO)
                .collect(Collectors.toList());
        return eventDTOs.removeFirst(); // I couldn't find a way to return only a single JPA query so this shall do
    }

    public EventDTO createEvent(Long userId, EventForm eventForm) {
        try {
            Event event = new Event();
            User user = new User();
            Location location = new Location();

            user.setUserId(userId);
            location.setLocationId(eventForm.getLocationId());

            event.setTitle(eventForm.getTitle());
            event.setDescription(eventForm.getDescription());
            event.setDescription(eventForm.getDescription());
            event.setCapacity(eventForm.getCapacity());
            event.setStart(eventForm.getStart());
            event.setEnd(eventForm.getEnd());
            event.setStatus(eventForm.getStatus());
            event.setPrice(eventForm.getPrice());

            // If there's time left fix this
            List<EventCategory> c = addCategories(eventForm.getCategories(), event);

            event.setEventCategories(c);
            event.setOrganizer(user);
            event.setLocation(location);
            return mapToDTO(eventRepository.save(event));
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create event due to foreign key error");
        }
    }

    public EventDTO updateEvent(Long id, Event event) {
        event.setEventId(id);
        return mapToDTO(eventRepository.save(event));
    }

    public Boolean deleteEvent(Long userId, Long id) {
        eventRepository.deleteById(id);
        try {
            Optional<EventDTO> event = getEventById(id);
            if (event.isPresent()) {
                if (event.get().getOrganizerId() == userId) {
                    eventRepository.deleteById(id);
                    return true;
                } else {
                    throw new AuthorizationDeniedException("You do not have permission to delete this event");
                }
            } else {
                throw new NotFoundException("Event with this ID does not exist");
            }
        } catch (DataAccessResourceFailureException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not delete this event because other data is dependent upon it");
        }
    }

    public EventDTO mapToDTO(Event event) {
        return new EventDTO(event);
    }

    public Event mapToEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setEventId(eventDTO.getId());
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setCapacity(eventDTO.getCapacity());
        event.setStart(eventDTO.getStart());
        event.setEnd(eventDTO.getEnd());
        event.setStatus(eventDTO.getStatus());
        event.setCreatedAt(eventDTO.getCreatedAt());
        event.setUpdatedAt(eventDTO.getUpdatedAt());
        return event;
    }
    private List<EventCategory> addCategories(List<Long> categories, Event event) {
        if (categories == null) return null;
        List<EventCategory> c = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            Long categoryNumber = categories.get(i);
            EventCategory eventCategory = new EventCategory();

            Category category = new Category();
            CategoryFA categoryfa = new CategoryFA();
            CategoryFI categoryfi = new CategoryFI();
            CategoryJA categoryja = new CategoryJA();
            category.setCategoryId(categoryNumber);
            categoryfa.setCategoryId(categoryNumber);
            categoryfi.setCategoryId(categoryNumber);
            categoryja.setCategoryId(categoryNumber);

            eventCategory.setCategory(category);
            eventCategory.setCategoryfi(categoryfi);
            eventCategory.setCategoryfa(categoryfa);
            eventCategory.setCategoryja(categoryja);
            eventCategory.setEvent(event);
            c.add(eventCategory);
        }
        return c;
    }
}