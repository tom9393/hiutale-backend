package com.hiutaleapp.controller;

import com.hiutaleapp.dto.EventDTO;
import com.hiutaleapp.dto.ReviewDTO;
import com.hiutaleapp.entity.Event;
import com.hiutaleapp.entity.Notification;
import com.hiutaleapp.entity.Review;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.service.EventService;
import com.hiutaleapp.service.NotificationService;
import com.hiutaleapp.service.ReviewService;
import com.hiutaleapp.util.DataViolationException;
import com.hiutaleapp.util.DatabaseConnectionException;
import com.hiutaleapp.util.NotFoundException;
import com.hiutaleapp.util.ReviewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private EventService eventService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ReviewDTO createReview(@RequestBody ReviewForm reviewForm) {
        if (reviewForm.getRating() > 5 || reviewForm.getRating() < 0) throw new DataViolationException("Rating must be between 5 and 0");
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            Review review = new Review();
            User user = new User();
            Event event = new Event();

            user.setUserId(Long.parseLong(auth.getName()));
            event.setEventId(reviewForm.getEventId());

            review.setUser(user);
            review.setEvent(event);
            review.setRating(reviewForm.getRating());
            review.setReviewText(reviewForm.getText());

            Optional<EventDTO> e = eventService.getEventById(reviewForm.getEventId());
            if (e.isEmpty()) {
                throw new DataViolationException("No event with this ID found");
            }

            Notification notification = new Notification();
            User user2 = new User();
            user2.setUserId(e.get().getOrganizerId());
            notification.setUser(user2);
            notification.setReadStatus(false);
            notification.setDisplayAfter(new Date());
            notification.setMessage("Someone has left a review to your event!");

            notificationService.createNotification(notification);
            return reviewService.createReview(review);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create review due to foreign key error; either event doesn't exist or you have already reviewed it");
        }
    }

    @PutMapping("/update/{id}")
    public ReviewDTO updateReview(@PathVariable Long id, @RequestBody Review review) {
        return reviewService.updateReview(id, review);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReview(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            Optional<ReviewDTO> review = reviewService.getReviewById(id);
            if (review.isPresent()) {
                if (review.get().getUserId() == Long.parseLong(auth.getName())) {
                    reviewService.deleteReview(id);
                } else {
                    throw new AuthorizationDeniedException("You do not have permission to delete this review");
                }
            } else {
                throw new NotFoundException("Review with this ID does not exist");
            }
        } catch (DataAccessResourceFailureException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not delete this review because other data is dependent upon it");
        }
    }
}