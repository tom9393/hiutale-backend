package com.hiutaleapp.dto;

import com.hiutaleapp.entity.EventAttendee;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventAttendeeDTO {
    private Long eventAttendeeId;
    private Long eventId;
    private Long userId;
    private String status;
    private Date registrationTime;

    public EventAttendeeDTO(EventAttendee eventAttendee) {
        this.eventAttendeeId = eventAttendee.getEventAttendeeId();
        this.eventId = eventAttendee.getEvent().getEventId();
        this.userId = eventAttendee.getUser().getUserId();
        this.status = eventAttendee.getStatus();
        this.registrationTime = eventAttendee.getCreatedAt();
    }
}