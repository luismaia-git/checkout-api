package com.qikserve.checkout.repository.cart;

import com.qikserve.checkout.model.entities.cart.CartSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartSummaryRepository extends JpaRepository<CartSummary, Long> {
    Optional<CartSummary> findByCartId(Long cartId);
}
