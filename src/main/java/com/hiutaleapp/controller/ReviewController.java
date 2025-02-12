package com.hiutaleapp.controller;

import com.hiutaleapp.dto.ReviewDTO;
import com.hiutaleapp.entity.Review;
import com.hiutaleapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

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
    public ReviewDTO createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping("/update/{id}")
    public ReviewDTO updateReview(@PathVariable Long id, @RequestBody Review review) {
        return reviewService.updateReview(id, review);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}