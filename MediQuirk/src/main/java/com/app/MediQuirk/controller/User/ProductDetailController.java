package com.app.MediQuirk.controller.User;

import com.app.MediQuirk.model.Product;
import com.app.MediQuirk.model.ProductReview;
import com.app.MediQuirk.model.Users;
import com.app.MediQuirk.services.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("products")
public class ProductDetailController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final ProductReviewService productReviewService;
    private final UserService userService;

    public ProductDetailController(ProductService productService,
                                   CategoryService categoryService,
                                   SupplierService supplierService,
                                   ProductReviewService productReviewService,
                                   UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.productReviewService = productReviewService;
        this.userService = userService;
    }

    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable Long id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "6") int size,
                                    Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        Page<ProductReview> reviewPage = productReviewService.getReviewsByProductId(id, PageRequest.of(page, size));

        model.addAttribute("product", product);
        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("productReview", new ProductReview());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());

        return "/User/product/detail";
    }

    @PostMapping("/review/add")
    public String addReview(@RequestParam Long productId, @ModelAttribute ProductReview productReview) {
        Users currentUser = getCurrentUser();
        if (currentUser != null) {
            productReviewService.addReview(productId, productReview, currentUser);
        }
        return "redirect:/products/detail/" + productId;
    }

    @PostMapping("/review/edit")
    public String editReview(@RequestParam Long productId, @ModelAttribute ProductReview productReview) {
        Users currentUser = getCurrentUser();
        if (currentUser != null && productReviewService.isReviewOwnedByUser(productReview.getReviewId(), currentUser)) {
            productReviewService.updateReview(productReview);
        }
        return "redirect:/products/detail/" + productId;
    }

    @PostMapping("/review/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        Users currentUser = getCurrentUser();
        ProductReview review = productReviewService.getReviewById(id);
        Long productId = review.getProduct().getProductId();
        if (currentUser != null && productReviewService.isReviewOwnedByUser(id, currentUser)) {
            productReviewService.deleteReview(id);
        }
        return "redirect:/products/detail/" + productId;
    }

    @GetMapping("/management")
    public String showProductManagement() {
        return "/api/product-api";
    }

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.findByUsername(currentUsername).orElse(null);
    }
}