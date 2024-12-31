package com.qikserve.checkout.exception.product.base;

import com.qikserve.checkout.exception.IResponseMessage;

public interface IProductId extends IResponseMessage {
    public String getProductId();
}