package com.hiutaleapp.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewForm {

    private Integer rating;
    private String text;
    private Long eventId;


    public ReviewForm(Integer rating, String text, Long eventId) {
        this.rating = rating;
        this.text = text;
        this.eventId = eventId;
    }
}
