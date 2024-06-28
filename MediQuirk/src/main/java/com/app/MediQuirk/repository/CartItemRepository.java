package com.app.MediQuirk.repository;

import com.app.MediQuirk.model.Cart;
import com.app.MediQuirk.model.CartItem;
import com.app.MediQuirk.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
