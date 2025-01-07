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


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BuyXGetYFreePromotion extends AbstractPromotion {
    private String id;

    @JsonProperty("required_qty")
    private int requiredQty;

    @JsonProperty("free_qty")
    private int freeQty;

    @Override
    public PromotionType getType() {
        return PromotionType.BUY_X_GET_Y_FREE;
    }

    @Override
    public PromotionApplied applyPromotion(int quantity, BigInteger pricePerUnit , BigDecimal total) {
        if(isApplicable(quantity, pricePerUnit)) {
            int sets = quantity / requiredQty;
            int freeItems = sets * freeQty;

            int quantityRemains = quantity + freeItems;

            BigDecimal discount = new BigDecimal(
                    pricePerUnit.multiply(
                            BigInteger.valueOf(freeQty).multiply(BigInteger.valueOf(sets))
                    )
            );

            BigDecimal subTotal = new BigDecimal(pricePerUnit.multiply(BigInteger.valueOf(quantityRemains)));
            System.out.println(quantityRemains);
            return PromotionApplied.builder()
                    .subTotal(subTotal.subtract(discount))
                    .price(pricePerUnit)
                    .quantity(quantityRemains)
                    .discount(discount)
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
