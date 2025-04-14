package com.hiutaleapp.util.forms;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    private BigDecimal price;
    private long userId;
    private long locationId;
    private List<Long> categories;

    public EventForm(String title, String description, Integer capacity, Date start, Date end, String status, BigDecimal price, long locationId, List<Long> categories) {
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.start = start;
        this.end = end;
        this.status = status;
        this.price = price;
        this.locationId = locationId;
        this.categories = categories;
    }
}
