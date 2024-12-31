package com.qikserve.checkout.exception.cart.base;

import com.qikserve.checkout.exception.IResponseMessage;

public interface ICartSummaryId extends IResponseMessage {
    public Long getCartSummaryId();
}