package com.hiutaleapp.util;

import com.hiutaleapp.entity.Category;
import com.hiutaleapp.entity.EventCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<Long> categories;

    public EventForm(String title, String description, Integer capacity, Date start, Date end, String status, long locationId, List<Long> categories) {
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.start = start;
        this.end = end;
        this.status = status;
        this.locationId = locationId;
        this.categories = categories;
    }
}
