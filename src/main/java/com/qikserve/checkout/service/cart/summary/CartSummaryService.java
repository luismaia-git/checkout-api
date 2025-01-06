package com.qikserve.checkout.service.cart.summary;

import com.qikserve.checkout.exception.cart.notfound.CartNotFoundException;
import com.qikserve.checkout.exception.cart.notfound.CartSummaryNotFoundException;
import com.qikserve.checkout.model.entities.cart.Cart;
import com.qikserve.checkout.model.entities.cart.CartSummary;
import com.qikserve.checkout.repository.cart.CartRepository;
import com.qikserve.checkout.repository.cart.CartSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartSummaryService implements ICartSummaryService{

    private final CartSummaryRepository cartSummaryRepository;

    @Override
    public CartSummary getCartSummaryById(Long cartSummaryId) {
        return cartSummaryRepository.findById(cartSummaryId).orElseThrow(()-> CartSummaryNotFoundException.of(cartSummaryId));
    }

    @Override
    public CartSummary getCartSummaryByCartId(Long cartId) {
        return cartSummaryRepository.findByCartId(cartId).orElseThrow(()-> CartSummaryNotFoundException.of(cartId));
    }

    @Override
    public CartSummary createCartSummary(CartSummary cartSummary) {
        return cartSummaryRepository.save(cartSummary);
    }
}
