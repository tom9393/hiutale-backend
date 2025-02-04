package com.hiutaleapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;

    private String title;

    private String description;

    private Integer capacity;

    private Date startTime;

    private Date endTime;

    private String status;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User organizer;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "event")
    private List<EventCategory> eventCategories;

    @OneToMany(mappedBy = "event")
    private List<EventAttendee> eventAttendees;

    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets;
}