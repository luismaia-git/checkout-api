package com.qikserve.checkout.service.cart;

import com.qikserve.checkout.exception.cart.CartAlreadyCheckedOutException;
import com.qikserve.checkout.exception.cart.CartNotOpenException;
import com.qikserve.checkout.exception.cart.CheckoutFailedCartEmpty;
import com.qikserve.checkout.exception.cart.notfound.CartItemNotFoundException;
import com.qikserve.checkout.exception.cart.notfound.CartNotFoundException;
import com.qikserve.checkout.exception.tenant.TenantMismatchException;
import com.qikserve.checkout.exception.tenant.TenantNotFoundException;
import com.qikserve.checkout.model.dto.CartSavingsDTO;
import com.qikserve.checkout.model.dto.CheckoutCartItem;
import com.qikserve.checkout.model.dto.Savings;
import com.qikserve.checkout.model.dto.promotion.PromotionApplied;
import com.qikserve.checkout.model.dto.promotion.base.AbstractPromotion;
import com.qikserve.checkout.model.entities.cart.*;
import com.qikserve.checkout.model.dto.product.Product;
import com.qikserve.checkout.multitenancy.context.TenantContext;
import com.qikserve.checkout.multitenancy.model.entity.Tenant;
import com.qikserve.checkout.multitenancy.service.TenantService;
import com.qikserve.checkout.repository.cart.CartItemRepository;
import com.qikserve.checkout.repository.cart.CartRepository;
import com.qikserve.checkout.repository.cart.CartSummaryRepository;
import com.qikserve.checkout.service.cart.summary.CartSummaryService;
import com.qikserve.checkout.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final CartSummaryService cartSummaryService;
    private final CartSummaryRepository cartSummaryRepository;
    private final TenantService tenantService;

    private String validateTenant() {
        String tenantId = TenantContext.getCurrentTenant();
        Tenant tenant = tenantService.findByTenantId(tenantId);
        if (tenant == null) {
            throw TenantNotFoundException.of(tenantId);
        }
        return tenant.getId();
    }

    public Cart createCart() {
        String tenantId = validateTenant();
        Cart cart = Cart.builder()
                .status(CartStatus.OPEN)
                .tenantId(tenantId)
                .build();

        return cartRepository.save(cart);
    }

    public void addCartItems(Long cartId, List<CartItem> cartItems) {
        String tenantId = validateTenant();
        Cart cart = this.getCartById(cartId);

        if(!cart.getTenantId().equals(tenantId)){
            throw TenantMismatchException.of(cart.getTenantId());
        }

        if(!cart.getStatus().equals(CartStatus.OPEN)){
            throw CartNotOpenException.of(cartId);
        }

        productService.validateProducts(cartItems.stream().map(CartItem::getProductId).toList());

        cartItemRepository.saveAll(cartItems.stream()
                .map(cartItem -> cartItem.withCartId(cartId))
                .collect(Collectors.toList()));
    }

    @Override
    public void removeItem(Long cartId, Long cartItemId) {
        Cart cart = this.getCartById(cartId);

        if(!cart.getStatus().equals(CartStatus.OPEN)){
            throw CartNotOpenException.of(cartId);
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> CartItemNotFoundException.of(cartItemId));
        cartItemRepository.delete(cartItem);
    }

    public CartSummary checkout(Long cartId) {

        Cart cartFound = this.getCartById(cartId);

        if(cartFound.getStatus().equals(CartStatus.CHECKOUT)){
            throw CartAlreadyCheckedOutException.of(cartFound.getId());
        }

        if(!(cartFound.getStatus().equals(CartStatus.OPEN))){
            throw CartNotOpenException.of(cartFound.getId());
        }

        if(cartFound.getItems().isEmpty()){
            throw CheckoutFailedCartEmpty.of(cartFound.getId());
        }

        productService.validateProducts(cartFound.getItems().stream().map(CartItem::getProductId).toList());

        CartSummary cartSummary =  evaluateCart(cartId);
        CartSummary savedCartSummary = cartSummaryService.createCartSummary(cartSummary);
        updateStatusCart(cartId, CartStatus.CHECKOUT);
        return savedCartSummary;
    }

    public CartSummary evaluateCart(Long cartId){
        List<CartItem> cartItems = this.getCartById(cartId).getItems();
        Map<String, PromotionApplied> productMapPromotions = new HashMap<>();
        for (CartItem item : cartItems) {

            String productId = item.getProductId();


            Product product = productService.getProductById(productId);

            List<? extends AbstractPromotion> promotions = product.getPromotions();
            int quantity = item.getQuantity();

            BigInteger pricePerUnit = product.getPrice();

            PromotionApplied promotionApplied =
                    PromotionApplied.builder()
                            .quantity(quantity)
                            .price(pricePerUnit)
                            .discount(BigDecimal.valueOf(0))
                            .subTotal(
                                    new BigDecimal(
                                            pricePerUnit.multiply(BigInteger.valueOf(quantity))
                                    )
                            )
                            .build();


            for(AbstractPromotion promotion : promotions) {
                PromotionApplied partialPromotionApplied = promotion.applyPromotion(
                        promotionApplied.getQuantity(),
                        promotionApplied.getPrice(),
                        promotionApplied.getSubTotal()
                );

                promotionApplied.merge(partialPromotionApplied);
            }

            promotionApplied.setProductId(product.getId());
            promotionApplied.setProductName(product.getName());

            if (productMapPromotions.containsKey(product.getId())) {
                PromotionApplied existingPromotion = productMapPromotions.get(product.getId());
                existingPromotion.setQuantity(existingPromotion.getQuantity() + promotionApplied.getQuantity());
                existingPromotion.setDiscount(existingPromotion.getDiscount().add(promotionApplied.getDiscount()));
                existingPromotion.setSubTotal(existingPromotion.getSubTotal().add(promotionApplied.getSubTotal()));
            } else {
                productMapPromotions.put(product.getId(), promotionApplied);
            }
        }


        Savings savings = Savings.builder()
                .finalPrice(BigDecimal.valueOf(0))
                .discount(BigDecimal.valueOf(0))
                .build();

        List<CheckoutCartItem> checkoutItems = new ArrayList<>();
        CartSummary cartSummary = CartSummary.builder()
                .total(0)
                .cartId(cartId)
                .build();

        for(Map.Entry<String, PromotionApplied> entry : productMapPromotions.entrySet()) {
            PromotionApplied promotionApplied = entry.getValue();

            checkoutItems.add(
                    CheckoutCartItem.builder()
                            .total(
                                    promotionApplied.getPrice().intValue() * promotionApplied.getQuantity()
                            )
                            .productId(entry.getKey())
                            .productName(promotionApplied.getProductName())
                            .totalSavings(promotionApplied.getDiscount())
                            .totalFinalPrice(promotionApplied.getSubTotal())
                            .quantity(promotionApplied.getQuantity())
                            .checkoutCart(cartSummary)
                            .unitPrice(promotionApplied.getPrice())
                            .build()
            );
        }

        checkoutItems.forEach(checkoutItem -> {
            cartSummary.setTotal(cartSummary.getTotal() + checkoutItem.getTotal());
            savings.setFinalPrice(savings.getFinalPrice().add(checkoutItem.getTotalFinalPrice()));
            savings.setDiscount(savings.getDiscount().add(checkoutItem.getTotalSavings()));
        });

        cartSummary.setPriceSummary(savings);
        cartSummary.setCheckoutDate(LocalDateTime.now());
        cartSummary.setItems(checkoutItems);
       return cartSummary;

    }


    @Override
    public void clearCart(Long cartId) {
        Cart cart = this.getCartById(cartId);

        if(!cart.getStatus().equals(CartStatus.OPEN)){
            throw CartNotOpenException.of(cartId);
        }

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartSummary getCartSummaryByCartId(Long cartId) {
        Optional<CartSummary> cart = cartSummaryRepository.findByCartId(cartId);
        return cart.orElseGet(() -> evaluateCart(cartId));
    }

    @Override
    public List<Cart> getAllCarts() {

        List<Cart> carts = cartRepository.findAll();
        return carts.stream()
                .map(cart -> cart.withItemsCount(cart.getItems().size()))
                .collect(Collectors.toList());
    }


    @Override
    public Cart getCartById (Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow( () -> CartNotFoundException.of(cartId));
        return cart.withItemsCount(cart.getItems().size());
    }

    public void updateStatusCart(Long cartId, CartStatus status){
        Cart cart = this.getCartById(cartId);
        cart.setStatus(status);
        cartRepository.save(cart);
    }

    public void cancel(Long cartId){
        validateTenant();
        Cart cart = this.getCartById(cartId);
        if(!cart.getStatus().equals(CartStatus.OPEN)){
            throw CartNotOpenException.of(cartId);
        }
        updateStatusCart(cartId, CartStatus.CANCELLED);
    }

    public Cart groupItems(Cart cart) {
        Map<String, CartItem> groupedItems = cart.getItems().stream()
                .collect(Collectors.toMap(
                        CartItem::getProductId,
                        cartItem -> cartItem,
                        (existing, replacement) -> {
                            existing.setQuantity(existing.getQuantity() + replacement.getQuantity());
                            return existing;
                        }
                ));

        cart.setItems(new ArrayList<>(groupedItems.values()));
        return cart;
    }

    @Override
    public CartSavingsDTO cartSavings(Long cartId) {
        Savings savings = evaluateCart(cartId).getPriceSummary();
        return CartSavingsDTO.builder()
                .cartId(cartId)
                .savings(savings)
                .total(this.getCartSummaryByCartId(cartId).getTotal())
                .build();
    }
    public void printProductMapPromotions(Map<String, PromotionApplied> productMapPromotions) {
        for (Map.Entry<String, PromotionApplied> entry : productMapPromotions.entrySet()) {
            String productId = entry.getKey();
            PromotionApplied promotionApplied = entry.getValue();

            System.out.println("Product ID: " + productId);
            System.out.println("Quantity: " + promotionApplied.getQuantity());
            System.out.println("Price per Unit: " + promotionApplied.getPrice());
            System.out.println("Discount: " + promotionApplied.getDiscount());
            System.out.println("Subtotal: " + promotionApplied.getSubTotal());
            System.out.println("-----------------------------");
        }
    }

}