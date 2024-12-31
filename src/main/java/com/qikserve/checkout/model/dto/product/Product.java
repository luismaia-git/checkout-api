package com.qikserve.checkout.model.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qikserve.checkout.model.dto.promotion.base.AbstractPromotion;
import jakarta.persistence.*;
import lombok.*;


import java.math.BigInteger;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@With
public class Product {

    private String id;
    private String name;
    private BigInteger price;

    @Builder.Default
    private List<? extends AbstractPromotion> promotions = List.of();

}
