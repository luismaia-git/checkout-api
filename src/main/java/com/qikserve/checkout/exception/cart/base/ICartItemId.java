package com.qikserve.checkout.exception.cart.base;

import com.qikserve.checkout.exception.IResponseMessage;

public interface ICartItemId extends IResponseMessage {
    public Long getCartItemId();
}