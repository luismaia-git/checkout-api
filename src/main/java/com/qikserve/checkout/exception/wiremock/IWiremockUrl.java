package com.qikserve.checkout.exception.wiremock;

import com.qikserve.checkout.exception.IResponseMessage;

public interface IWiremockUrl extends IResponseMessage {
    public String getWiremockUrl();
}