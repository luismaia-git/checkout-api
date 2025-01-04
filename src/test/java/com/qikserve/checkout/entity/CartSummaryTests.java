package com.qikserve.checkout.entity;

import com.qikserve.checkout.factory.CartFactory;
import com.qikserve.checkout.factory.CartItemFactory;
import com.qikserve.checkout.model.dto.CheckoutCartItem;
import com.qikserve.checkout.model.dto.Savings;
import com.qikserve.checkout.model.dto.product.Product;
import com.qikserve.checkout.model.entities.cart.Cart;
import com.qikserve.checkout.model.entities.cart.CartItem;
import com.qikserve.checkout.model.entities.cart.CartSummary;
import com.qikserve.checkout.repository.cart.CartItemRepository;
import com.qikserve.checkout.repository.cart.CartRepository;
import com.qikserve.checkout.repository.cart.CartSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CartSummaryTests {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartSummaryRepository cartSummaryRepository;
    private Cart cart;
    private final List<Product> products = Arrays.asList(
            Product.builder().id("product1").name("ProductName1").price(BigInteger.TEN.multiply(BigInteger.TWO)).build(),
            Product.builder().id("product2").name("ProductName2").price(BigInteger.TEN.multiply(BigInteger.TEN)).build(),
            Product.builder().id("product3").name("ProductName3").price(BigInteger.TEN.divide(BigInteger.TWO)).build(),
            Product.builder().id("product4").name("ProductName4").price(BigInteger.TEN).build()
    );

    private final List<CartItem> cartItems = new ArrayList<>();

    @BeforeEach
    public void beforeUp() {
        this.cart = CartFactory.makeCart();
        cartRepository.save(cart);

        Random random = new Random();

        for(int i = 0; i < 15; i++){
            Product product = products.get( i% products.size());
            int quantity = random.nextInt(10) + 1;
            cartItems.add(
                    CartItemFactory.makeCartItem(cart.getId(), product.getId()).withQuantity(quantity)
            );
        }

        cartItemRepository.saveAll(cartItems);

        cart.setItems(cartItems);

    }

    @Test
    public void shouldCreateCartSummarySuccessfully() {
        CartSummary cartSummary = CartSummary.builder()
                .cartId(cart.getId())
                .checkoutDate(LocalDateTime.now())
                .build();

        CartSummary cartSummarySaved = cartSummaryRepository.save(cartSummary);

        assertNotNull(cartSummarySaved);
        assertEquals(cart.getId(), cartSummarySaved.getCartId());
        assertNotNull(cartSummarySaved.getCheckoutDate());
        assertTrue(cartSummarySaved.getCheckoutDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    public void shouldCreateCompleteCartSummarySuccessfully() {

        List<CheckoutCartItem> checkoutItems = new ArrayList<>();

        CartSummary cartSummary = CartSummary.builder()
                .cartId(cart.getId())
                .checkoutDate(LocalDateTime.now())
                .build();

        cartItems.forEach(cartItem -> {
            Product product = products.stream().filter(p -> p.getId().equals(cartItem.getProductId())).findFirst().orElse(null);
            if(product != null){
                CheckoutCartItem checkoutCartItem = CheckoutCartItem.builder()
                        .checkoutCart(cartSummary)
                        .quantity(cartItem.getQuantity())
                        .productId(product.getId())
                        .productName(product.getName())
                        .total(product.getPrice().multiply(BigInteger.valueOf(cartItem.getQuantity())).intValue())
                        .build();
                checkoutItems.add(checkoutCartItem);
            }
        });

        Savings savings = Savings.builder()
                .finalPrice(BigDecimal.ZERO)
                .discount(BigDecimal.ZERO)
                .build();

        cartSummary.setItems(checkoutItems);

        checkoutItems.forEach(checkoutItem -> {
            cartSummary.setTotal(cartSummary.getTotal() + checkoutItem.getTotal());
            savings.setFinalPrice(savings.getFinalPrice().add(
                    checkoutItem.getTotalFinalPrice() != null ? checkoutItem.getTotalFinalPrice() : BigDecimal.ZERO));
            savings.setDiscount(savings.getDiscount().add(
                    checkoutItem.getTotalSavings() != null ? checkoutItem.getTotalSavings() : BigDecimal.ZERO));
        });

        cartSummary.setPriceSummary(savings);
        cartSummary.setItems(checkoutItems);

        CartSummary cartSummarySaved = cartSummaryRepository.save(cartSummary);

        assertNotNull(cartSummarySaved);
        assertEquals(cart.getId(), cartSummarySaved.getCartId());
        assertEquals(cartSummary.getItems().size(), cartSummarySaved.getItems().size());
        assertEquals(cartSummary.getTotal(), cartSummarySaved.getTotal());
        assertEquals(cartSummary.getPriceSummary().getFinalPrice(), cartSummarySaved.getPriceSummary().getFinalPrice());
        assertEquals(cartSummary.getPriceSummary().getDiscount(), cartSummarySaved.getPriceSummary().getDiscount());
        assertNotNull(cartSummarySaved.getCheckoutDate());
        assertTrue(cartSummarySaved.getCheckoutDate().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertFalse(cartSummarySaved.getItems().isEmpty());
        assertTrue(cartSummarySaved.getTotal() > 0);
    }

    @Test
    public void shouldGetCartSummaryByIdSuccessfully() {
        CartSummary cartSummary = CartSummary.builder()
                .cartId(cart.getId())
                .checkoutDate(LocalDateTime.now())
                .build();

        cartSummaryRepository.save(cartSummary);

        CartSummary cartSummaryRetrieved = cartSummaryRepository.findById(cartSummary.getId()).orElse(null);

        assertNotNull(cartSummaryRetrieved);
        assertEquals(cartSummary.getId(), cartSummaryRetrieved.getId());
        assertEquals(cartSummary.getCartId(), cartSummaryRetrieved.getCartId());


        assertEquals(cartSummary.getCheckoutDate().getDayOfMonth(), cartSummaryRetrieved.getCheckoutDate().getDayOfMonth());
        assertEquals(cartSummary.getCheckoutDate().getMonth(), cartSummaryRetrieved.getCheckoutDate().getMonth());
        assertEquals(cartSummary.getCheckoutDate().getYear(), cartSummaryRetrieved.getCheckoutDate().getYear());
        assertEquals(cartSummary.getCheckoutDate().getHour(), cartSummaryRetrieved.getCheckoutDate().getHour());
        assertEquals(cartSummary.getCheckoutDate().getMinute(), cartSummaryRetrieved.getCheckoutDate().getMinute());
        assertEquals(cartSummary.getCheckoutDate().getSecond(), cartSummaryRetrieved.getCheckoutDate().getSecond());

    }

    @Test
    public void shouldGetAllCartSummariesSuccessfully() {
        CartSummary cartSummary = CartSummary.builder()
                .cartId(cart.getId())
                .checkoutDate(LocalDateTime.now())
                .build();

        cartSummaryRepository.save(cartSummary);

        Cart cart2 = CartFactory.makeCart();

        cartRepository.save(cart2);

        CartSummary anotherCartSummary = CartSummary.builder()
                .cartId(cart2.getId())
                .checkoutDate(LocalDateTime.now())
                .build();

        cartSummaryRepository.save(anotherCartSummary);

        List<CartSummary> cartSummaries = cartSummaryRepository.findAll();

        assertNotNull(cartSummaries);
        assertEquals(2, cartSummaries.size());
        assertTrue(cartSummaries.contains(cartSummary));
        assertTrue(cartSummaries.contains(anotherCartSummary));
    }

    @Test
    public void shouldDeleteCartSummarySuccessfully() {
        CartSummary cartSummary = CartSummary.builder()
                .cartId(cart.getId())
                .checkoutDate(LocalDateTime.now())
                .build();

        cartSummaryRepository.save(cartSummary);

        assertEquals(1, cartSummaryRepository.count());

        cartSummaryRepository.deleteById(cartSummary.getId());

        assertEquals(0, cartSummaryRepository.count());
    }

    @Test
    public void shouldUpdateCartSummarySuccessfully() {
        CartSummary cartSummary = CartSummary.builder()
                .cartId(cart.getId())
                .checkoutDate(LocalDateTime.now())
                .build();

        cartSummaryRepository.save(cartSummary);

        CartSummary cartSummaryUpdated = cartSummaryRepository.save(
                cartSummary.withCheckoutDate(cartSummary.getCheckoutDate().minusDays(1))
        );

        assertEquals(cartSummary.getId(), cartSummaryUpdated.getId());
        assertEquals(cartSummary.getCartId(), cartSummaryUpdated.getCartId());
        assertEquals(cartSummary.getCheckoutDate().getDayOfMonth(), cartSummaryUpdated.getCheckoutDate().plusDays(1).getDayOfMonth());
        assertEquals(cartSummary.getCheckoutDate().getMonth(), cartSummaryUpdated.getCheckoutDate().getMonth());
        assertEquals(cartSummary.getCheckoutDate().getYear(), cartSummaryUpdated.getCheckoutDate().getYear());
        assertEquals(cartSummary.getCheckoutDate().getHour(), cartSummaryUpdated.getCheckoutDate().getHour());
        assertEquals(cartSummary.getCheckoutDate().getMinute(), cartSummaryUpdated.getCheckoutDate().getMinute());
        assertEquals(cartSummary.getCheckoutDate().getSecond(), cartSummaryUpdated.getCheckoutDate().getSecond());
    }

}