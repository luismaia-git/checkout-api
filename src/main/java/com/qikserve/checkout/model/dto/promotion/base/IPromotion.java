package com.qikserve.checkout.model.dto.promotion.base;
import com.qikserve.checkout.model.dto.promotion.PromotionApplied;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface IPromotion {
    String getId();
    PromotionType getType();
    PromotionApplied applyPromotion(int quantity, BigInteger pricePerUnit, BigDecimal total);
    Boolean isApplicable(int quantity, BigInteger pricePerUnit);
}
