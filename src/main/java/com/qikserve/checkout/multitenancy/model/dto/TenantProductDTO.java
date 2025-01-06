package com.qikserve.checkout.multitenancy.model.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigInteger;

@Data
@Builder
public class TenantProductDTO {
    private String id;
    private String name;
    private BigInteger price;
}
