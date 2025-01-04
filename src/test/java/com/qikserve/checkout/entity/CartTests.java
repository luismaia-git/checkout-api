package com.qikserve.checkout.entity;


import com.qikserve.checkout.factory.CartFactory;
import com.qikserve.checkout.model.entities.cart.Cart;
import com.qikserve.checkout.model.entities.cart.CartStatus;
import com.qikserve.checkout.repository.cart.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CartTests {
    @Autowired
    private CartRepository cartRepository;

    @Test
    public void shouldCreateCartSuccessfully() {
        Cart cart = CartFactory.makeCart();
        cartRepository.save(cart);
        Cart cartSaved = cartRepository.findById(cart.getId()).orElse(null);

        assertNotNull(cartSaved);
        assertEquals(cart.getId(), cartSaved.getId());
        assertEquals(cart.getTenantId(), cartSaved.getTenantId());
        assertEquals(cart.getStatus(), cartSaved.getStatus());
    }

    @Test
    public void shouldUpdateCartSuccessfully() {
        Cart cart = CartFactory.makeCart();

        cartRepository.save(cart);

        Cart cartSaved = cartRepository.findById(cart.getId()).orElse(null);
        assertNotNull(cartSaved);

        Cart cartUpdated = cartRepository.save(
                cartSaved.withTenantId("2").withStatus(CartStatus.CANCELLED)
        );

        assertEquals(cart.getId(), cartUpdated.getId());
        assertEquals("2", cartUpdated.getTenantId());
        assertEquals(CartStatus.CANCELLED, cartUpdated.getStatus());
    }

    @Test
    public void shouldDeleteCartSuccessfully() {
        Cart cart = CartFactory.makeCart();
        cartRepository.save(cart);

        cartRepository.deleteById(cart.getId());

        Cart cartDeleted = cartRepository.findById(cart.getId()).orElse(null);
        assertNull(cartDeleted);
    }

    @Test
    public void shouldGetCartByIdSuccessfully() {
        Cart cart = CartFactory.makeCart();

        cartRepository.save(cart);

        Cart cartRetrieved = cartRepository.findById(cart.getId()).orElse(null);

        assertNotNull(cartRetrieved);
        assertEquals(cart.getId(), cartRetrieved.getId());
        assertEquals(cart.getTenantId(), cartRetrieved.getTenantId());
        assertEquals(cart.getStatus(), cartRetrieved.getStatus());
    }

    @Test
    public void shouldGetAllCartsSuccessfully() {
        Cart cart = CartFactory.makeCart();

        cartRepository.save(cart);

        Cart anotherCart = CartFactory.makeCart();

        cartRepository.save(anotherCart);

        List<Cart> carts = cartRepository.findAll();

        assertNotNull(carts);
        assertEquals(2, carts.size());
        assertTrue(carts.contains(cart));
        assertTrue(carts.contains(anotherCart));
    }
}
