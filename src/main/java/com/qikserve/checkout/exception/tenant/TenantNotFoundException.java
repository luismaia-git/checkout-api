package com.qikserve.checkout.exception.tenant;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TenantNotFoundException extends TenantException {

    public static TenantNotFoundException of(String tenantId) {
        return TenantNotFoundException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .messageCode("error.tenantIdNotFound")
                .tenantId(tenantId)
                .build();
    }

}
