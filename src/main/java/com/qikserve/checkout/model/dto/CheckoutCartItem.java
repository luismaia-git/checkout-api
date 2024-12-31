package com.qikserve.checkout.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.qikserve.checkout.model.entities.cart.CartSummary;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CheckoutCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private String productName;
    private int quantity;
    private int total;

    private BigDecimal totalSavings;
    private BigDecimal totalFinalPrice;
    private BigInteger unitPrice;

    @ManyToOne
    @JoinColumn(name = "checkoutCartId", nullable = false)
    @JsonBackReference
    private CartSummary checkoutCart;

}
