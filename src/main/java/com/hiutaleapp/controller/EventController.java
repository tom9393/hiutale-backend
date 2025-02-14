package com.hiutaleapp.controller;

import com.hiutaleapp.dto.EventDTO;
import com.hiutaleapp.entity.*;
import com.hiutaleapp.service.EventService;
import com.hiutaleapp.util.DatabaseConnectionException;
import com.hiutaleapp.util.DataViolationException;
import com.hiutaleapp.util.EventForm;
import com.hiutaleapp.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/all")
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEventsWithCounts();
    }

    @GetMapping("/one/{id}")
    public EventDTO getEventById(@PathVariable Long id) {
        return eventService.getEventWithCounts(id);
    }

    @PostMapping("/create")
    public EventDTO createEvent(@RequestBody EventForm eventForm) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            Event event = new Event();
            User user = new User();
            Location location = new Location();

            user.setUserId(Long.parseLong(auth.getName()));
            location.setLocationId(eventForm.getLocationId());

            event.setTitle(eventForm.getTitle());
            event.setDescription(eventForm.getDescription());
            event.setDescription(eventForm.getDescription());
            event.setCapacity(eventForm.getCapacity());
            event.setStartTime(eventForm.getStart());
            event.setEndTime(eventForm.getEnd());
            event.setStatus(eventForm.getStatus());

            // If there's time left fix this
            List<EventCategory> c = addCategories(eventForm.getCategories(), event);

            event.setEventCategories(c);
            event.setOrganizer(user);
            event.setLocation(location);
            return eventService.createEvent(event);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create event due to foreign key error");
        }
    }

    @PutMapping("/update/{id}")
    public EventDTO updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            Optional<EventDTO> event = eventService.getEventById(id);
            if (event.isPresent()) {
                if (event.get().getOrganizerId() == Long.parseLong(auth.getName())) {
                    eventService.deleteEvent(id);
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

    private List<EventCategory> addCategories(List<Long> categories, Event event) {
        if (categories == null) return null;
        List<EventCategory> c = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            Long categoryNumber = categories.get(i);
            EventCategory eventCategory = new EventCategory();
            Category category = new Category();
            category.setCategoryId(categoryNumber);
            eventCategory.setCategory(category);
            eventCategory.setEvent(event);
            c.add(eventCategory);
        }
        return c;
    }
}