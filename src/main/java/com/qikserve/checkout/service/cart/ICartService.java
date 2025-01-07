package com.qikserve.checkout.service.cart;

import com.qikserve.checkout.model.dto.CartSavingsDTO;
import com.qikserve.checkout.model.entities.cart.*;

import java.util.List;

public interface ICartService {

    CartSummary checkout(Long cartId);
    Cart createCart();
    List<Cart> getAllCarts();
    List<CartItem> addCartItems(Long cartId, List<CartItem> cartItems);
    Cart getCartById (Long cartId);
    void updateStatusCart(Long cartId, CartStatus status);
    void cancel(Long cartId);

    void clearCart(Long cartId);

    CartSummary getCartSummaryByCartId(Long cartId);
    CartSummary evaluateCart(Long cartId);
    Cart groupItems(Cart cart);
    CartSavingsDTO cartSavings(Long cartId);
}
