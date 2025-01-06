package com.qikserve.checkout.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CartSavingsDTO {
    Long cartId;
    Savings savings;
    int total;
}
