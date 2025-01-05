package com.qikserve.checkout.model.entities.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.qikserve.checkout.model.dto.CheckoutCartItem;
import com.qikserve.checkout.model.dto.Savings;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "carts_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@EqualsAndHashCode
public class CartSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_id", nullable = false, unique = true)
    @JsonManagedReference
    private Long cartId;

    @OneToMany(
            mappedBy = "checkoutCart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private List<CheckoutCartItem> items = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "discount", column = @Column(name = "discount")),
        @AttributeOverride(name = "finalPrice", column = @Column(name = "total_final_price"))
    })
    private Savings priceSummary;

    @Column
    private int total;

    @Column(name = "checkout_date" , updatable = false)
    @EqualsAndHashCode.Exclude
    private LocalDateTime checkoutDate;
}
