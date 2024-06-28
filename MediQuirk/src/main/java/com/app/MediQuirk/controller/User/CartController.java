package com.app.MediQuirk.controller.User;

import com.app.MediQuirk.model.AddToCartRequest;
import com.app.MediQuirk.model.Cart;
import com.app.MediQuirk.model.RemoveFromCartRequest;
import com.app.MediQuirk.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public Cart addToCart(@RequestBody AddToCartRequest request) {
        return cartService.addToCart(request.getCartId(), request.getProductId(), request.getQuantity());
    }

    @PostMapping("/remove")
    public Cart removeFromCart(@RequestBody RemoveFromCartRequest request) {
        return cartService.removeFromCart(request.getCartId(), request.getProductId());
    }

    @GetMapping("/{cartId}")
    public Cart getCart(@PathVariable Long cartId) {
        return cartService.getCart(cartId);
    }
}
