package com.qikserve.checkout.exception.tenant;

import com.qikserve.checkout.exception.BaseException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter(onMethod_ = {@Override})
public abstract class TenantException extends BaseException implements ITenantId {
    private final String tenantId;

    @Override
    protected Object[] getArgs() {
        return new Object[]{tenantId};
    }
}
