package com.hiutaleapp.util;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventForm {

    private String title;
    private String description;
    private Integer capacity;
    private Date start;
    private Date end;
    private String status;
    private long userId;
    private long locationId;

    public EventForm(String title, String description, Integer capacity, Date start, Date end, String status, long locationId) {
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.start = start;
        this.end = end;
        this.status = status;
        this.locationId = locationId;
    }
}
