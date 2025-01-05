package com.qikserve.checkout.factory;

import com.qikserve.checkout.multitenancy.model.entity.Tenant;

public class TenantFactory {

    public static Tenant makeTenant() {
        return Tenant.builder()
                .name("tenant")
                .baseUrl("http://localhost:8080")
                .build();
    }
}