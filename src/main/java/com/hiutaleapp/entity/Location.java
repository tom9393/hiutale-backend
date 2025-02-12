package com.hiutaleapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @SequenceGenerator(initialValue=1, name="location_seq", sequenceName="location_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="location_seq")
    private Long locationId;

    private String name;

    private String address;

    private String city;

    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}