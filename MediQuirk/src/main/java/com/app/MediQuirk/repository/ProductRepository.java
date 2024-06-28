package com.app.MediQuirk.repository;

import com.app.MediQuirk.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductNameAndSupplierName(String productName, String supplierName);
}
