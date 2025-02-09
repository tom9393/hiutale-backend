package com.hiutaleapp.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @SequenceGenerator(initialValue=1, name="user_seq", sequenceName="user_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_seq")
    private Long userId;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String role;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @OneToMany(mappedBy = "organizer")
    private List<Event> organizedEvents;

    @OneToMany(mappedBy = "user")
    private List<EventAttendee> eventAttendees;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}