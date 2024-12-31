package com.qikserve.checkout.exception.cart.notfound;

import com.qikserve.checkout.exception.cart.base.CartException;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartNotFoundException extends CartException {

    public static CartNotFoundException of(Long cartId) {
        return CartNotFoundException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .messageCode("error.cartIdNotFound")
                .cartId(cartId)
                .build();
    }

}
