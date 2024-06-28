package com.app.MediQuirk.model;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long cartId;
    private Long productId;
    private int quantity;
}
