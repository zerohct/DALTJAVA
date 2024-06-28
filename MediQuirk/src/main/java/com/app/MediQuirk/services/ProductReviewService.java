package com.app.MediQuirk.services;

import com.app.MediQuirk.model.ProductReview;
import com.app.MediQuirk.repository.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductReviewService {
    @Autowired
    private ProductReviewRepository reviewRepository;

    public ProductReview addReview(ProductReview review) {
        review.setReviewDate(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<ProductReview> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProduct_ProductId(productId);
    }

    public List<ProductReview> getReviewsByUser(Long userId) {
        return reviewRepository.findByUser_UserId(userId);
    }

    public ProductReview getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public ProductReview updateReview(ProductReview review) {
        ProductReview existingReview = getReviewById(review.getReviewId());
        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());
        existingReview.setReviewDate(LocalDateTime.now());
        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}