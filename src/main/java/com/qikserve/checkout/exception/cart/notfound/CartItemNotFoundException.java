package com.qikserve.checkout.exception.cart.notfound;

import com.qikserve.checkout.exception.cart.base.CartItemException;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartItemNotFoundException extends CartItemException {


    public static CartItemNotFoundException of(Long cartItemId) {
        return CartItemNotFoundException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .messageCode("error.cartItemIdNotFound")
                .cartItemId(cartItemId)
                .build();
    }

}
