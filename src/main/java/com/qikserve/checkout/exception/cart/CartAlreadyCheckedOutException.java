package com.qikserve.checkout.exception.cart;

import com.qikserve.checkout.exception.cart.base.CartException;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CartAlreadyCheckedOutException extends CartException {
    public static CartAlreadyCheckedOutException of(Long cartId) {
        return CartAlreadyCheckedOutException.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .messageCode("error.cartAlreadyCheckedOut")
                .cartId(cartId)
                .build();
    }
}
