package com.app.MediQuirk.services;

import com.app.MediQuirk.model.Product;
import com.app.MediQuirk.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    // Retrieve all products from the database
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Retrieve all products including out of stock
    public List<Product> getAllProductsIncludingOutOfStock() {
        return productRepository.findAll();
    }

    // Retrieve a product by its id
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Add a new product to the database with image
    public Product addProduct(@NotNull Product product, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(image);
            product.setImageUrl(imageUrl);
        }
        return productRepository.save(product);
    }

    // Update an existing product with image
    public Product updateProduct(Long id, @NotNull Product productDetails, MultipartFile image) throws IOException {
        Product existingProduct = getProductById(id)
                .orElseThrow(() -> new IllegalStateException("Product with ID " + id + " does not exist."));

        // Update only non-null fields
        if (productDetails.getProductName() != null) {
            existingProduct.setProductName(productDetails.getProductName());
        }
        if (productDetails.getPrice() != null) {
            existingProduct.setPrice(productDetails.getPrice());
        }
        if (productDetails.getDescription() != null) {
            existingProduct.setDescription(productDetails.getDescription());
        }
        if (productDetails.getCategory() != null) {
            existingProduct.setCategory(productDetails.getCategory());
        }
        existingProduct.setPrescriptionRequired(productDetails.isPrescriptionRequired());
        existingProduct.setStockQuantity(productDetails.getStockQuantity());
        if (productDetails.getSupplier() != null) {
            existingProduct.setSupplier(productDetails.getSupplier());
        }

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(image);
            existingProduct.setImageUrl(imageUrl);
        }

        return productRepository.save(existingProduct);
    }

    // Delete a product by its id
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    // Search products by name
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }
}