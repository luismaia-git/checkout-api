package com.qikserve.checkout.entity;

import com.qikserve.checkout.model.entities.cart.Cart;
import com.qikserve.checkout.model.entities.cart.CartItem;
import com.qikserve.checkout.model.entities.cart.CartStatus;
import com.qikserve.checkout.repository.cart.CartItemRepository;
import com.qikserve.checkout.repository.cart.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CartItemTests {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Test
    public void shouldGetAllCartItemsSuccessfully() {

        Cart cart = Cart.builder()
                .tenantId("1")
                .status(CartStatus.OPEN)
                .build();
        cartRepository.save(cart);

        CartItem cartItem = CartItem.builder()
                .cartId(cart.getId())
                .productId("product1")
                .quantity(1)
                .build();

        cartItemRepository.save(cartItem);

        CartItem anotherCartItem = CartItem.builder()
                .cartId(cart.getId())
                .productId("product2")
                .quantity(2)
                .build();

        cartItemRepository.save(anotherCartItem);

        List<CartItem> cartItems = cartItemRepository.findAll();

        assertEquals(3, cartItems.size());
        assertTrue(cartItems.contains(cartItem));
        assertTrue(cartItems.contains(anotherCartItem));
    }

    @Test
    public void shouldCreateCartItemSuccessfully() {
        Cart cart = Cart.builder()
                .tenantId("1")
                .status(CartStatus.OPEN)
                .build();
        cartRepository.save(cart);

        CartItem cartItem = CartItem.builder()
                .cartId(cart.getId())
                .productId("product1")
                .quantity(1)
                .build();

        cartItemRepository.save(cartItem);

        CartItem cartItemSaved = cartItemRepository.findById(cartItem.getId()).orElse(null);

        assertNotNull(cartItemSaved);
        assertEquals(cartItem.getId(), cartItemSaved.getId());
        assertEquals(cartItem.getProductId(), cartItemSaved.getProductId());
        assertEquals(cartItem.getQuantity(), cartItemSaved.getQuantity());
    }

    @Test
    public void shouldUpdateCartItemSuccessfully() {
        Cart cart = Cart.builder()
                .tenantId("1")
                .status(CartStatus.OPEN)
                .build();
        cartRepository.save(cart);

        CartItem cartItem = CartItem.builder()
                .cartId(cart.getId())
                .productId("product1")
                .quantity(1)
                .build();

        cartItemRepository.save(cartItem);

        CartItem cartItemSaved = cartItemRepository.findById(cartItem.getId()).orElse(null);
        assertNotNull(cartItemSaved);

        cartItemSaved.setProductId("product2");
        cartItemSaved.setQuantity(2);

        CartItem cartItemUpdated = cartItemRepository.save(cartItemSaved);

        assertEquals(cartItem.getId(), cartItemUpdated.getId());
        assertEquals("product2", cartItemUpdated.getProductId());
        assertEquals(2, cartItemUpdated.getQuantity());
    }

    @Test
    public void shouldDeleteCartItemSuccessfully() {
        Cart cart = Cart.builder()
                .tenantId("1")
                .status(CartStatus.OPEN)
                .build();
        cartRepository.save(cart);

        CartItem cartItem = CartItem.builder()
                .cartId(cart.getId())
                .productId("product1")
                .quantity(1)
                .build();

        cartItemRepository.save(cartItem);

        cartItemRepository.deleteById(cartItem.getId());

        CartItem cartItemDeleted = cartItemRepository.findById(cartItem.getId()).orElse(null);
        assertNull(cartItemDeleted);
    }

    @Test
    public void shouldGetCartItemByIdSuccessfully() {
        Cart cart = Cart.builder()
                .tenantId("1")
                .status(CartStatus.OPEN)
                .build();
        cartRepository.save(cart);

        CartItem cartItem = CartItem.builder()
                .cartId(cart.getId())
                .productId("product1")
                .quantity(1)
                .build();

        cartItemRepository.save(cartItem);

        CartItem cartItemRetrieved = cartItemRepository.findById(cartItem.getId()).orElse(null);

        assertNotNull(cartItemRetrieved);
        assertEquals(cartItem.getId(), cartItemRetrieved.getId());
        assertEquals(cartItem.getProductId(), cartItemRetrieved.getProductId());
        assertEquals(cartItem.getQuantity(), cartItemRetrieved.getQuantity());
    }


}