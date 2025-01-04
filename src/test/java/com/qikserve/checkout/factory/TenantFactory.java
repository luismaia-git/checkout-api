package com.qikserve.checkout.factory;

import com.qikserve.checkout.model.dto.ProductMapper;
import com.qikserve.checkout.model.entities.cart.Cart;
import com.qikserve.checkout.model.entities.cart.CartStatus;
import com.qikserve.checkout.multitenancy.model.entity.Tenant;

import java.util.ArrayList;

public class TenantFactory {

    public static Tenant makeTenant() {
        return Tenant.builder()
                .name("tenant")
                .baseUrl("http://localhost:8080")
                .productMapper(
                        ProductMapper.builder()
                                .externalId("id")
                                .externalName("name")
                                .build()
                )
                .build();
    }
}