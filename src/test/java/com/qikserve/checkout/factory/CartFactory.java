package com.qikserve.checkout.factory;

import com.qikserve.checkout.model.entities.cart.Cart;
import com.qikserve.checkout.model.entities.cart.CartStatus;

import java.util.ArrayList;

public class CartFactory {

    public static Cart makeCart() {
        return makeCart(new Cart());
    }

    public static Cart makeCart(Cart override) {
        return Cart.builder()
                .tenantId(override.getTenantId() != null ? override.getTenantId() : "default-tenant")
                .status(override.getStatus() != null ? override.getStatus() : CartStatus.OPEN)
                .items(override.getItems() != null ? override.getItems() : new ArrayList<>())
                .build();
    }
}