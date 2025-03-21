package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Favourite;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FavouriteDTO {
    private Long id;
    private Long eventId;
    private Date favouriteDate;

    public FavouriteDTO(Favourite favourite) {
        this.id = favourite.getFavouriteId();
        this.eventId = favourite.getEvent().getEventId();
        this.favouriteDate = favourite.getCreatedAt();
    }
}