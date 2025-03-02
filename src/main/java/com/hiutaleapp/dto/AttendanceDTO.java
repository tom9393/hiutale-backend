package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Attendance;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AttendanceDTO {
    private Long id;
    private Long eventId;
    private Date registrationTime;

    public AttendanceDTO(Attendance attendance) {
        this.id = attendance.getAttendeeId();
        this.eventId = attendance.getEvent().getEventId();
        this.registrationTime = attendance.getCreatedAt();
    }
}