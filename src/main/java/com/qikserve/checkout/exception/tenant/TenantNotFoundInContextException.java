package com.qikserve.checkout.exception.tenant;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TenantNotFoundInContextException extends TenantException {

    public static TenantNotFoundInContextException of(String tenantId) {
        return TenantNotFoundInContextException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .messageCode("error.tenantIdNotFoundInContext")
                .tenantId(tenantId)
                .build();
    }

}
