package com.qikserve.checkout.exception.tenant;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.FORBIDDEN)
public class TenantMismatchException extends TenantException {

    public static TenantMismatchException of(String tenantId) {
        return TenantMismatchException.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .messageCode("error.tenantMismatch")
                .tenantId(tenantId)
                .build();
    }

}
