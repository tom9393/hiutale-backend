package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Favourite;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TicketDTO {
    private Long ticketId;
    private Long eventId;
    private Long userId;
    private String ticketType;
    private Double price;
    private String status;
    private Date purchaseDate;

    public TicketDTO(Favourite favourite) {
        this.ticketId = favourite.getTicketId();
        this.eventId = favourite.getEvent().getEventId();
        this.userId = favourite.getUser().getUserId();
        this.ticketType = favourite.getTicketType();
        this.price = favourite.getPrice();
        this.status = favourite.getStatus();
        this.purchaseDate = favourite.getCreatedAt();
    }
}