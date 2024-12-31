package com.qikserve.checkout.service.cart.summary;

import com.qikserve.checkout.model.entities.cart.CartSummary;

public interface ICartSummaryService {
    CartSummary getCartSummaryById(Long cartSummaryId);
    CartSummary getCartSummaryByCartId(Long cartId);
    CartSummary createCartSummary(CartSummary cartSummary);
}
