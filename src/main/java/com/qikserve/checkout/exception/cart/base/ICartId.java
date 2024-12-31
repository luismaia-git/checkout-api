package com.qikserve.checkout.exception.cart.base;

import com.qikserve.checkout.exception.IResponseMessage;

public interface ICartId extends IResponseMessage {
    public Long getCartId();
}