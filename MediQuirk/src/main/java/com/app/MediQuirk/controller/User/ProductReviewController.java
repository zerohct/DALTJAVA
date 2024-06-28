package com.app.MediQuirk.controller.User;


import com.app.MediQuirk.model.ProductReview;

import com.app.MediQuirk.services.ProductService;
import com.app.MediQuirk.services.UserService;
import com.app.MediQuirk.services.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/reviews")
public class ProductReviewController {

    @Autowired
    private ProductReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/product/{productId}")
    public String showProductReviews(@PathVariable Long productId, Model model) {
        model.addAttribute("reviews", reviewService.getReviewsByProduct(productId));
        model.addAttribute("product", productService.getProductById(productId));
        return "product-reviews";
    }

    @GetMapping("/add/{productId}")
    public String showAddReviewForm(@PathVariable Long productId, Model model) {
        model.addAttribute("productReview", new ProductReview());
        model.addAttribute("product", productService.getProductById(productId));
        return "add-review";
    }

    @PostMapping("/add")
    public String addReview(@Valid @ModelAttribute("productReview") ProductReview review,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-review";
        }


        review.setUser(userService.getCurrentUser());
        reviewService.addReview(review);
        return "redirect:/reviews/product/" + review.getProduct().getProductId();
    }

    @GetMapping("/edit/{reviewId}")
    public String showEditReviewForm(@PathVariable Long reviewId, Model model) {
        model.addAttribute("productReview", reviewService.getReviewById(reviewId));
        return "edit-review";
    }

    @PostMapping("/edit")
    public String updateReview(@Valid @ModelAttribute("productReview") ProductReview review,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "edit-review";
        }
        reviewService.updateReview(review);
        return "redirect:/reviews/product/" + review.getProduct().getProductId();
    }

    @PostMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId) {
        ProductReview review = reviewService.getReviewById(reviewId);
        Long productId = review.getProduct().getProductId();
        reviewService.deleteReview(reviewId);
        return "redirect:/reviews/product/" + productId;
    }
}
