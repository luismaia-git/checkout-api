package com.qikserve.checkout.exception.cart;

import com.qikserve.checkout.exception.cart.base.CartItemException;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCartItemQuantityException extends CartItemException {
    public static InvalidCartItemQuantityException of(Long cartItemId) {
        return InvalidCartItemQuantityException.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .messageCode("error.invalidCartItemQuantity")
                .cartItemId(cartItemId)
                .build();
    }
}
