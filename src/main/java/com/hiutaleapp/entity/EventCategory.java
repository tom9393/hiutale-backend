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
@Table(name = "event_categories")
public class EventCategory {

    @Id
    @SequenceGenerator(initialValue=1, name="eventcategory_seq", sequenceName="eventcategory_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="eventcategory_seq")
    private Long eventCategoryId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "category_fi_id")
    private CategoryFI categoryfi;

    @ManyToOne
    @JoinColumn(name = "category_fa_id")
    private CategoryFA categoryfa;

    @ManyToOne
    @JoinColumn(name = "category_ja_id")
    private CategoryJA categoryja;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}