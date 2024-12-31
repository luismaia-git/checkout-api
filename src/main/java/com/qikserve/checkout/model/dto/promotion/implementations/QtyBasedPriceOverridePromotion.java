package com.qikserve.checkout.model.dto.promotion.implementations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class QtyBasedPriceOverridePromotion extends AbstractPromotion {

    @JsonProperty("required_qty")
    private int requiredQty;

    private BigInteger price;

    @Override
    public PromotionType getType() {
        return PromotionType.QTY_BASED_PRICE_OVERRIDE;
    }

    @Override
    public PromotionApplied applyPromotion(int quantity, BigInteger pricePerUnit, BigDecimal total ) {
        if(isApplicable(quantity, pricePerUnit)) {
            int sets = quantity / requiredQty;
            int remaining = quantity % requiredQty;

            BigInteger subTotal = price
                    .multiply(BigInteger.valueOf(sets))
                    .add(BigInteger.valueOf((long) remaining * pricePerUnit.intValue()));


            return PromotionApplied.builder()
                    .price(pricePerUnit)
                    .quantity(quantity)
                    .discount(total.subtract(new BigDecimal(subTotal)))
                    .subTotal(new BigDecimal(subTotal))
                    .build();
        }

        return PromotionApplied.builder()
                .subTotal(total)
                .price(pricePerUnit)
                .quantity(quantity)
                .discount(BigDecimal.ZERO)
                .build();
    }

    @Override
    public Boolean isApplicable(int quantity, BigInteger pricePerUnit) {
        return quantity >= this.getRequiredQty();
    }
}
