package com.hiutaleapp.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceForm {

    private Long id;

    public AttendanceForm(Long id) {
        this.id = id;
    }
}
