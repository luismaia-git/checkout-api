package com.qikserve.checkout.model.dto.promotion.implementations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.io.BigIntegerParser;
import com.qikserve.checkout.model.dto.promotion.PromotionApplied;
import com.qikserve.checkout.model.dto.promotion.base.AbstractPromotion;
import com.qikserve.checkout.model.dto.promotion.base.PromotionType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlatPercentPromotion extends AbstractPromotion {

    private BigDecimal amount;

    @Override
    public PromotionType getType() {
        return PromotionType.FLAT_PERCENT;
    }

    @Override
    public PromotionApplied applyPromotion(int quantity, BigInteger pricePerUnit, BigDecimal total) {

        if(amount.equals(BigDecimal.ZERO)) return PromotionApplied.builder().subTotal(total).price(pricePerUnit).quantity(quantity).build();

        BigDecimal discount = new BigDecimal(pricePerUnit.multiply(BigInteger.valueOf(quantity))).multiply(amount).divide(BigDecimal.valueOf(100));

        return PromotionApplied.builder()
                .price(pricePerUnit)
                .subTotal(total.subtract(discount))
                .quantity(quantity)
                .discount(discount)
                .build();

    }

    @Override
    public Boolean isApplicable(int quantity, BigInteger pricePerUnit) {
        return true;
    }
}
