package com.qikserve.checkout.exception.cart.base;

import com.qikserve.checkout.exception.BaseException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter(onMethod_ = {@Override})
public abstract class CartException extends BaseException implements ICartId {
    private final Long cartId;

    @Override
    protected Object[] getArgs() {
        return new Object[]{cartId};
    }
}
