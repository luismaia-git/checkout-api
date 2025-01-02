package com.qikserve.checkout.model.dto.promotion.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.qikserve.checkout.model.dto.promotion.PromotionApplied;
import com.qikserve.checkout.model.dto.promotion.implementations.BuyXGetYFreePromotion;
import com.qikserve.checkout.model.dto.promotion.implementations.FlatPercentPromotion;
import com.qikserve.checkout.model.dto.promotion.implementations.QtyBasedPriceOverridePromotion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes(
        {
            @JsonSubTypes.Type(value = BuyXGetYFreePromotion.class, name = "BUY_X_GET_Y_FREE"),
            @JsonSubTypes.Type(value = FlatPercentPromotion.class, name = "FLAT_PERCENT"),
            @JsonSubTypes.Type(value = QtyBasedPriceOverridePromotion.class, name = "QTY_BASED_PRICE_OVERRIDE"),
        }
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractPromotion {

    private String id;

    @Builder.ObtainVia(method = "getType")
    private PromotionType type;
    
    public abstract PromotionApplied applyPromotion(int quantity, BigInteger pricePerUnit, BigDecimal total);

    public abstract Boolean isApplicable(int quantity, BigInteger pricePerUnit);
}