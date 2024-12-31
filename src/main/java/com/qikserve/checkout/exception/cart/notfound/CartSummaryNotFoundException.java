package com.qikserve.checkout.exception.cart.notfound;

import com.qikserve.checkout.exception.cart.base.CartSummaryException;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartSummaryNotFoundException extends CartSummaryException {

    public static CartSummaryNotFoundException of(Long cartSummaryId) {
        return CartSummaryNotFoundException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .messageCode("error.cartSummaryIdNotFound")
                .cartSummaryId(cartSummaryId)
                .build();
    }

}

