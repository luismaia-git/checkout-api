package com.qikserve.checkout.service;

import com.qikserve.checkout.exception.cart.CartNotOpenException;
import com.qikserve.checkout.exception.cart.notfound.CartNotFoundException;
import com.qikserve.checkout.extension.TenantContextExtension;
import com.qikserve.checkout.factory.CartFactory;
import com.qikserve.checkout.factory.CartItemFactory;
import com.qikserve.checkout.factory.TenantFactory;
import com.qikserve.checkout.model.entities.cart.Cart;
import com.qikserve.checkout.model.entities.cart.CartItem;
import com.qikserve.checkout.model.entities.cart.CartStatus;
import com.qikserve.checkout.model.entities.cart.CartSummary;
import com.qikserve.checkout.multitenancy.context.TenantContext;
import com.qikserve.checkout.multitenancy.model.entity.Tenant;
import com.qikserve.checkout.multitenancy.repository.TenantRepository;
import com.qikserve.checkout.multitenancy.service.TenantService;
import com.qikserve.checkout.multitenancy.service.TenantServiceImpl;
import com.qikserve.checkout.repository.cart.CartItemRepository;
import com.qikserve.checkout.repository.cart.CartRepository;
import com.qikserve.checkout.repository.cart.CartSummaryRepository;
import com.qikserve.checkout.service.cart.CartServiceImpl;
import com.qikserve.checkout.service.cart.item.CartItemServiceImpl;
import com.qikserve.checkout.service.cart.summary.CartSummaryService;
import com.qikserve.checkout.service.product.IProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(TenantContextExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private IProductService productService;

    @Mock
    private CartSummaryRepository cartSummaryRepository;

    @Mock
    private CartSummaryService cartSummaryService = new CartSummaryService(cartSummaryRepository);

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private TenantServiceImpl tenantService = new TenantServiceImpl(tenantRepository);

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    public void testGetCart_WhenCartExists_ShouldReturnCart() {
        // Given
        var cartId = 1L;
        var cart = CartFactory.makeCart().withId(cartId);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        // When
        Optional<Cart> result = Optional.ofNullable(cartService.getCartById(cartId));

        // Then
        assertTrue(result.isPresent());
        assertEquals(cart, result.get());
    }

    @Test
    public void testGetCart_WhenCartDoesNotExist_ShouldReturnEmpty() {
        // Given
        var cartId = 1L;
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // Then
        assertThrows(CartNotFoundException.class, () -> cartService.getCartById(cartId));
    }

    @Test
    public void testCreateCart_ShouldReturnNewCart() {
        // Given
        var tenantId = "tenant1";
        var tenant = TenantFactory.makeTenant().withId(tenantId);

        when(tenantService.findByTenantId(tenantId)).thenReturn(tenant);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0, Cart.class).withId(1L));

        // When
        var result = cartService.createCart();

        // Then
        assertNotNull(result);
        assertEquals(CartStatus.OPEN, result.getStatus());
        assertEquals(1L, result.getId());
    }

    @Test
    public void testAddCartItem_WhenCartIsOpen_ShouldSaveCartItem() {
        // Given
        var tenantId = "tenant1";
        var tenant = TenantFactory.makeTenant().withId(tenantId);
        when(tenantService.findByTenantId(eq(tenantId))).thenReturn(tenant);

        var id = 1L;
        var cartItem = CartItemFactory.makeCartItem(id, "1");
        var cart = CartFactory.makeCart().withId(id).withTenantId(tenantId);

        when(cartRepository.findById(eq(id))).thenReturn(Optional.of(cart));
        when(cartItemRepository.saveAll(any())).thenReturn(List.of(cartItem));
        // When
        var result = cartService.addCartItems(id, List.of(cartItem));
        System.out.println(result);
        // Then
        assertEquals(cartItem, result.get(0));
    }

    @Test
    public void testAddCartItem_WhenCartIsNotOpen_ShouldThrowException() {
        // Given
        var tenantId = "tenant1";
        var tenant = TenantFactory.makeTenant().withId(tenantId);
        when(tenantService.findByTenantId(eq(tenantId))).thenReturn(tenant);

        var id = 1L;
        var cartItem = CartItemFactory.makeCartItem(id, "1");
        var cart = CartFactory.makeCart().withId(id).withTenantId(tenantId).withStatus(CartStatus.CANCELLED);

        when(cartRepository.findById(id)).thenReturn(Optional.of(cart));

        // When & Then
        var e = assertThrows(CartNotOpenException.class, () -> cartService.addCartItems(id, List.of(cartItem)));
        assertEquals(id, e.getCartId());
    }

    @Test
    public void testCancelCart_WhenCartExists_ShouldUpdateStatus() {
        // Given

        var tenantId = "tenant1";
        var tenant = TenantFactory.makeTenant().withId(tenantId);
        when(tenantService.findByTenantId(eq(tenantId))).thenReturn(tenant);

        var cartId = 1L;
        var cart = CartFactory.makeCart().withId(cartId).withTenantId(tenantId);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        // When
        cartService.cancel(cartId);

        // Then
        assertEquals(CartStatus.CANCELLED, cart.getStatus());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void testClearCart_WhenCartExists_ShouldClearCartItems() {
        // Given
        var cartId = 1L;
        var cartItem = CartItemFactory.makeCartItem(cartId, "1");
        var cart = CartFactory.makeCart().withId(cartId).withItems(new ArrayList<>(List.of(cartItem)));
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        // When
        cartService.clearCart(cartId);

        // Then
        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository, times(1)).save(cartCaptor.capture());
        assertTrue(cartCaptor.getValue().getItems().isEmpty());
    }

    @Test
    void testCheckout_WhenCartNotOpen_ShouldThrowCartNotOpenException() {
        // Given
        var id = 1L;
        var cart = CartFactory.makeCart().withId(id).withStatus(CartStatus.CANCELLED);

        when(cartRepository.findById(id)).thenReturn(Optional.of(cart));

        // When & Then
        assertThrows(CartNotOpenException.class, () -> cartService.checkout(id));
    }

}

