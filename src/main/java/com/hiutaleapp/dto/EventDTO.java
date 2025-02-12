package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Event;
import com.hiutaleapp.entity.EventCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private Integer capacity;
    private Date startTime;
    private Date endTime;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private Long organizerId;
    private Long locationId;
    private List<Long> eventCategoryIds;

    public EventDTO(Event event) {
        this.id = event.getEventId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.capacity = event.getCapacity();
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.status = event.getStatus();
        this.createdAt = event.getCreatedAt();
        this.updatedAt = event.getUpdatedAt();
        this.organizerId = event.getOrganizer().getUserId();
        this.locationId = event.getLocation().getLocationId();
        if (event.getEventCategories() != null) {
            this.eventCategoryIds = event.getEventCategories().stream().map(EventCategory::getEventCategoryId).toList();
        } else {
            this.eventCategoryIds = null;
        }
    }
}