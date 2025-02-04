package com.hiutaleapp.service;

import com.hiutaleapp.dto.ReviewDTO;
import com.hiutaleapp.entity.Review;
import com.hiutaleapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReviewDTO> getReviewById(Long id) {
        return reviewRepository.findById(id).map(this::mapToDTO);
    }

    public ReviewDTO createReview(Review review) {
        return mapToDTO(reviewRepository.save(review));
    }

    public ReviewDTO updateReview(Long id, Review review) {
        review.setReviewId(id);
        return mapToDTO(reviewRepository.save(review));
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public ReviewDTO mapToDTO(Review review) {
        return new ReviewDTO(review);
    }

    public Review mapToEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setReviewId(reviewDTO.getReviewId());
        review.getUser().setUserId(reviewDTO.getUserId());
        review.setRating(reviewDTO.getRating());
        review.setReviewText(reviewDTO.getReviewText());
        review.setCreatedAt(reviewDTO.getCreatedAt());
        return review;
    }
}