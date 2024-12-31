package com.qikserve.checkout.exception.tenant;

import com.qikserve.checkout.exception.IResponseMessage;

public interface ITenantId extends IResponseMessage {
    String getTenantId();
}