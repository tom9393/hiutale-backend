package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Event;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private Integer capacity;
    private Date start;
    private Date end;
    private String status;
    private BigDecimal price;
    private Date createdAt;
    private Date updatedAt;
    private Long organizerId;
    private Long locationId;
    private List<Long> categories;
    private Long attendanceCount;
    private Long favouriteCount;

    public EventDTO(Event event) {
        this.id = event.getEventId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.capacity = event.getCapacity();
        this.start = event.getStart();
        this.end = event.getEnd();
        this.status = event.getStatus();
        this.price = event.getPrice();
        this.createdAt = event.getCreatedAt();
        this.updatedAt = event.getUpdatedAt();
        this.organizerId = event.getOrganizer().getUserId();
        this.locationId = event.getLocation().getLocationId();
        this.attendanceCount = (long) 0;
        this.favouriteCount = (long) 0;
        this.categories = event.getEventCategories() != null ? event.getEventCategories().stream().map(eg -> eg.getCategory().getCategoryId()).toList() : null; // Ugly garbage
    }
}