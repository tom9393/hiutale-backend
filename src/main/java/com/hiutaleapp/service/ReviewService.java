package com.hiutaleapp.service;

import com.hiutaleapp.dto.EventDTO;
import com.hiutaleapp.dto.ReviewDTO;
import com.hiutaleapp.entity.Event;
import com.hiutaleapp.entity.Notification;
import com.hiutaleapp.entity.Review;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.repository.ReviewRepository;
import com.hiutaleapp.util.errors.DataViolationException;
import com.hiutaleapp.util.errors.DatabaseConnectionException;
import com.hiutaleapp.util.errors.NotFoundException;
import com.hiutaleapp.util.forms.ReviewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EventService eventService;

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReviewDTO> getReviewById(Long id) {
        return reviewRepository.findById(id).map(this::mapToDTO);
    }

    public ReviewDTO createReview(Long userId, ReviewForm reviewForm) {
        if (reviewForm.getRating() > 5 || reviewForm.getRating() < 0) throw new DataViolationException("Rating must be between 5 and 0");
        try {
            Review review = new Review();
            User user = new User();
            Event event = new Event();

            user.setUserId(userId);
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
            return mapToDTO(reviewRepository.save(review));
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create review due to foreign key error; either event doesn't exist or you have already reviewed it");
        }
    }

    public ReviewDTO updateReview(Long id, Review review) {
        review.setReviewId(id);
        return mapToDTO(reviewRepository.save(review));
    }

    public Boolean deleteReview(Long userId, Long id) {
        try {
            Optional<ReviewDTO> review = getReviewById(id);
            if (review.isPresent()) {
                if (review.get().getUserId() == userId) {
                    reviewRepository.deleteById(id);
                    return true;
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

    public ReviewDTO mapToDTO(Review review) {
        return new ReviewDTO(review);
    }

    public Review mapToEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setReviewId(reviewDTO.getId());
        review.getUser().setUserId(reviewDTO.getUserId());
        review.setRating(reviewDTO.getRating());
        review.setReviewText(reviewDTO.getReviewText());
        review.setCreatedAt(reviewDTO.getCreatedAt());
        return review;
    }
}