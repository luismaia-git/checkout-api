package com.qikserve.checkout.exception.cart;

import com.qikserve.checkout.exception.cart.base.CartException;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CheckoutFailedCartEmpty extends CartException {
    public static CheckoutFailedCartEmpty of(Long cartId) {
        return CheckoutFailedCartEmpty.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .messageCode("error.checkoutFailedCartEmpty")
                .cartId(cartId)
                .build();
    }
}
