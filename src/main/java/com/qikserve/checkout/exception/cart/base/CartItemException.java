package com.qikserve.checkout.exception.cart.base;

import com.qikserve.checkout.exception.BaseException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter(onMethod_ = {@Override})
public abstract class CartItemException extends BaseException implements ICartItemId {
    private final Long cartItemId;

    @Override
    protected Object[] getArgs() {
        return new Object[]{cartItemId};
    }
}