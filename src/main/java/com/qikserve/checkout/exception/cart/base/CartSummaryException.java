package com.qikserve.checkout.exception.cart.base;

import com.qikserve.checkout.exception.BaseException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter(onMethod_ = {@Override})
public abstract class CartSummaryException extends BaseException implements ICartSummaryId {
    private final Long cartSummaryId;

    @Override
    protected Object[] getArgs() {
        return new Object[]{cartSummaryId};
    }
}