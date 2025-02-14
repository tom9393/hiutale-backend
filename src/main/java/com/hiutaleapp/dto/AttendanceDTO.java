package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Attendance;
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

    public EventAttendeeDTO(Attendance attendance) {
        this.eventAttendeeId = attendance.getEventAttendeeId();
        this.eventId = attendance.getEvent().getEventId();
        this.userId = attendance.getUser().getUserId();
        this.status = attendance.getStatus();
        this.registrationTime = attendance.getCreatedAt();
    }
}