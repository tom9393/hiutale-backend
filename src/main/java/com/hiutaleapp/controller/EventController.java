package com.hiutaleapp.controller;

import com.hiutaleapp.dto.EventDTO;
import com.hiutaleapp.entity.*;
import com.hiutaleapp.service.EventService;
import com.hiutaleapp.util.UserContext;
import com.hiutaleapp.util.forms.EventForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return eventService.createEvent(UserContext.getUserId(), eventForm);
    }

    @PutMapping("/update/{id}")
    public EventDTO updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteEvent(@PathVariable Long id) {
        return eventService.deleteEvent(UserContext.getUserId(), id);
    }
}