package com.qikserve.checkout.exception.wiremock;

import com.qikserve.checkout.exception.cart.base.CartException;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WiremockUnavailableException extends WiremockException {
    public static WiremockUnavailableException of(String wiremockUrl) {
        return WiremockUnavailableException.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .messageCode("error.wiremockUnavailable")
                .wiremockUrl(wiremockUrl)
                .build();
    }
}
