package com.qikserve.checkout.exception.cart;

import com.qikserve.checkout.exception.cart.base.CartException;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CartNotOpenException extends CartException {
    public static CartNotOpenException of(Long cartId) {
        return CartNotOpenException.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .messageCode("error.cartNotOpen")
                .cartId(cartId)
                .build();
    }
}
