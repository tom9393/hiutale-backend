package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private Long eventId;
    private Long userId;
    private Integer rating;
    private String reviewText;
    private Date createdAt;
    private Date updatedAt;

    public ReviewDTO(Review review) {
        this.id = review.getReviewId();
        this.eventId = review.getEvent().getEventId();
        this.userId = review.getUser().getUserId();
        this.rating = review.getRating();
        this.reviewText = review.getReviewText();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}