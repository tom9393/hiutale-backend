package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Ticket;
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

    public TicketDTO(Ticket ticket) {
        this.ticketId = ticket.getTicketId();
        this.eventId = ticket.getEvent().getEventId();
        this.userId = ticket.getUser().getUserId();
        this.ticketType = ticket.getTicketType();
        this.price = ticket.getPrice();
        this.status = ticket.getStatus();
        this.purchaseDate = ticket.getCreatedAt();
    }
}