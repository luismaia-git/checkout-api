package com.qikserve.checkout.service.cart.item;

import com.qikserve.checkout.model.entities.cart.*;

import java.util.List;

public interface ICartItemService {

    CartItem createCartItem(CartItem cartItem);
    void removeCartItem(Long cartItemId);
    CartItem getCartItemById (Long cartItemId);
    void updateQuantityCartItem(Long cartItemId, int quantity);
    List<CartItem> getAllCartItems();
}
