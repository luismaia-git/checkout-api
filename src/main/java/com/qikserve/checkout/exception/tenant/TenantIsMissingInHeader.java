package com.qikserve.checkout.exception.tenant;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TenantIsMissingInHeader extends TenantException {

    public static TenantIsMissingInHeader of(String tenantId) {
        return TenantIsMissingInHeader.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .messageCode("error.tenantIdIsMissingInHeader")
                .tenantId(tenantId)
                .build();
    }

}
