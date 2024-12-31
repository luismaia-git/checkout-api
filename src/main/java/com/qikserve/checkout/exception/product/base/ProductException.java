package com.qikserve.checkout.exception.product.base;

import com.qikserve.checkout.exception.BaseException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter(onMethod_ = {@Override})
public abstract class ProductException extends BaseException implements IProductId {
    private final String productId;

    @Override
    protected Object[] getArgs() {
        return new Object[]{productId};
    }
}
