package com.app.MediQuirk.repository;

import com.app.MediQuirk.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByProduct_ProductId(Long productId);
    List<ProductReview> findByUser_UserId(Long userId);
}
