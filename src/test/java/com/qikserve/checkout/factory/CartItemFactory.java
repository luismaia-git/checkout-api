package com.qikserve.checkout.factory;

import com.qikserve.checkout.model.entities.cart.CartItem;

public class CartItemFactory {

    public static CartItem makeCartItem(Long cartId, String productId) {
        return CartItem.builder()
                .cartId(cartId)
                .productId(productId)
                .quantity(1)
                .build();
    }
}