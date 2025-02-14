package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Attendance;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AttendanceDTO {
    private Long id;
    private Date registrationTime;

    public AttendanceDTO(Attendance attendance) {
        this.id = attendance.getAttendeeId();
        this.registrationTime = attendance.getCreatedAt();
    }
}