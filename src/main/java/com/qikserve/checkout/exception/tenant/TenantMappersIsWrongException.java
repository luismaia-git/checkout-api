package com.qikserve.checkout.exception.tenant;

import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TenantMappersIsWrongException extends TenantException {

    public static TenantMappersIsWrongException of(String tenantId) {
        return TenantMappersIsWrongException.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .messageCode("error.tenantMappersIsWrong")
                .tenantId(tenantId)
                .build();
    }

}
