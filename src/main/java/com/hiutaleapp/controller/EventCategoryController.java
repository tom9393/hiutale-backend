package com.hiutaleapp.controller;

import com.hiutaleapp.dto.EventCategoryDTO;
import com.hiutaleapp.entity.EventCategory;
import com.hiutaleapp.service.EventCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event-categories")
public class EventCategoryController {
    @Autowired
    private EventCategoryService eventCategoryService;

    @GetMapping("/all")
    public List<EventCategoryDTO> getAllEventCategories() {
        return eventCategoryService.getAllEventCategories();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<EventCategoryDTO> getEventCategoryById(@PathVariable Long id) {
        return eventCategoryService.getEventCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public EventCategoryDTO createEventCategory(@RequestBody EventCategory eventCategory) {
        return eventCategoryService.createEventCategory(eventCategory);
    }

    @PutMapping("/update/{id}")
    public EventCategoryDTO updateEventCategory(@PathVariable Long id, @RequestBody EventCategory eventCategory) {
        return eventCategoryService.updateEventCategory(id, eventCategory);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEventCategory(@PathVariable Long id) {
        eventCategoryService.deleteEventCategory(id);
    }
}