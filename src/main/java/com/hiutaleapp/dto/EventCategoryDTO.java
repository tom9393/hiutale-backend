package com.hiutaleapp.dto;

import com.hiutaleapp.entity.EventCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventCategoryDTO {
    private Long eventCategoryId;
    private Long categoryId;
    private Long eventId;

    public EventCategoryDTO(EventCategory eventCategory) {
        this.eventCategoryId = eventCategory.getEventCategoryId();
        this.categoryId = eventCategory.getCategory().getCategoryId();
        this.eventId = eventCategory.getEvent().getEventId();
    }
}