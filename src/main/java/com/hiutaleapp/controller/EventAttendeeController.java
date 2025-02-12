package com.hiutaleapp.controller;

import com.hiutaleapp.dto.EventAttendeeDTO;
import com.hiutaleapp.entity.EventAttendee;
import com.hiutaleapp.service.EventAttendeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event-attendees")
public class EventAttendeeController {
    @Autowired
    private EventAttendeeService eventAttendeeService;

    @GetMapping("/all")
    public List<EventAttendeeDTO> getAllEventAttendees() {
        return eventAttendeeService.getAllEventAttendees();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<EventAttendeeDTO> getEventAttendeeById(@PathVariable Long id) {
        return eventAttendeeService.getEventAttendeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public EventAttendeeDTO createEventAttendee(@RequestBody EventAttendee eventAttendee) {
        return eventAttendeeService.createEventAttendee(eventAttendee);
    }

    @PutMapping("/update/{id}")
    public EventAttendeeDTO updateEventAttendee(@PathVariable Long id, @RequestBody EventAttendee eventAttendee) {
        return eventAttendeeService.updateEventAttendee(id, eventAttendee);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEventAttendee(@PathVariable Long id) {
        eventAttendeeService.deleteEventAttendee(id);
    }
}