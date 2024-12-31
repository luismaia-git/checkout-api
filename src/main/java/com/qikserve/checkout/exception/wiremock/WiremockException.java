package com.qikserve.checkout.exception.wiremock;

import com.qikserve.checkout.exception.BaseException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter(onMethod_ = {@Override})
public abstract class WiremockException extends BaseException implements IWiremockUrl {
    private final String wiremockUrl;

    @Override
    protected Object[] getArgs() {
        return new Object[]{wiremockUrl};
    }
}
