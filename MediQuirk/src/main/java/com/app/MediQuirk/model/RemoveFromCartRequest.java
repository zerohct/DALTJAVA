package com.app.MediQuirk.model;

import lombok.Data;

@Data
public class RemoveFromCartRequest {
    private Long cartId;
    private Long productId;
}
