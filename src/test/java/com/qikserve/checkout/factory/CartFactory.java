package com.qikserve.checkout.factory;

import com.qikserve.checkout.model.entities.cart.Cart;
import com.qikserve.checkout.model.entities.cart.CartStatus;

import java.util.ArrayList;

public class CartFactory {

    public static Cart makeCart() {
        return Cart.builder()
                .tenantId("tenant1")
                .status(CartStatus.OPEN)
                .items(new ArrayList<>())
                .build();
    }
}