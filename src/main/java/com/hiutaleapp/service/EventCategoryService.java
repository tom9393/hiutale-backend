package com.hiutaleapp.service;

import com.hiutaleapp.dto.EventCategoryDTO;
import com.hiutaleapp.entity.EventCategory;
import com.hiutaleapp.repository.EventCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventCategoryService {
    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    public List<EventCategoryDTO> getAllEventCategories() {
        return eventCategoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EventCategoryDTO> getEventCategoryById(Long id) {
        return eventCategoryRepository.findById(id).map(this::mapToDTO);
    }

    public EventCategoryDTO createEventCategory(EventCategory eventCategory) {
        return mapToDTO(eventCategoryRepository.save(eventCategory));
    }

    public EventCategoryDTO updateEventCategory(Long id, EventCategory eventCategory) {
        eventCategory.setEventCategoryId(id);
        return mapToDTO(eventCategoryRepository.save(eventCategory));
    }

    public void deleteEventCategory(Long id) {
        eventCategoryRepository.deleteById(id);
    }

    public EventCategoryDTO mapToDTO(EventCategory eventCategory) {
        return new EventCategoryDTO(eventCategory);
    }

    public EventCategory mapToEntity(EventCategoryDTO eventCategoryDTO) {
        EventCategory eventCategory = new EventCategory();
        eventCategory.setEventCategoryId(eventCategoryDTO.getEventCategoryId());
        eventCategory.getCategory().setCategoryId(eventCategoryDTO.getCategoryId());
        eventCategory.getEvent().setEventId(eventCategoryDTO.getEventId());
        return eventCategory;
    }

    public List<EventCategoryDTO> createEventCategories(List<EventCategory> categories) {
        return eventCategoryRepository.saveAll(categories).stream().map(EventCategoryDTO::new).collect(Collectors.toList());
    }
}