package com.app.MediQuirk.services;

import com.app.MediQuirk.model.Cart;
import com.app.MediQuirk.model.CartItem;
import com.app.MediQuirk.model.Product;
import com.app.MediQuirk.repository.CartRepository;
import com.app.MediQuirk.repository.CartItemRepository;
import com.app.MediQuirk.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart addToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId).orElse(new Cart());
        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                    .orElse(new CartItem());

            cartItem.setProduct(product);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setCart(cart);
            cartItemRepository.save(cartItem);

            cart.getCartItems().add(cartItem);
            cart.setTotalPrice(cart.getCartItems().stream()
                    .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart removeFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow();
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, productRepository.findById(productId).orElseThrow())
                .orElseThrow();

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        cart.setTotalPrice(cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        cartRepository.save(cart);
        return cart;
    }

    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow();
    }
}
