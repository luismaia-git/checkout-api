package com.qikserve.checkout.model.dto.promotion;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class PromotionApplied {
    private BigDecimal subTotal = BigDecimal.valueOf(0);
    private BigInteger price = BigInteger.valueOf(0);
    private BigDecimal discount = BigDecimal.valueOf(0);
    private String productId;
    private String productName;
    private int quantity = 0;

    public void merge(PromotionApplied promotionApplied) {
        this.price = promotionApplied.getPrice();
        this.subTotal = promotionApplied.getSubTotal();
        this.quantity = promotionApplied.getQuantity();
        this.discount = this.discount.add(promotionApplied.getDiscount());
    }

}
