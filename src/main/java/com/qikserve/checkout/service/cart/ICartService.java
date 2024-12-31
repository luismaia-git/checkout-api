package com.qikserve.checkout.service.cart;

import com.qikserve.checkout.model.dto.Savings;
import com.qikserve.checkout.model.entities.cart.*;

import java.util.List;

public interface ICartService {
    /**
     * @param cartId the cart to calculate the summary.
     * @return resume of the cart.
     */
    CartSummary checkout(Long cartId);
    Cart createCart();
    List<Cart> getAllCarts();
    void addCartItems(Long cartId, List<CartItem> cartItems);
    void removeItem(Long cartId, Long cartItemId);
    Cart getCartById (Long cartId);
    void updateStatusCart(Long cartId, CartStatus status);
    void cancel(Long cartId);

    void clearCart(Long cartId);

    CartSummary getCartSummaryByCartId(Long cartId);
    CartSummary evaluateCart(Long cartId);
    Cart groupItems(Cart cart);
}
