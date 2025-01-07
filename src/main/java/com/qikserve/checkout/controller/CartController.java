package com.qikserve.checkout.controller;

import com.qikserve.checkout.model.dto.CartSavingsDTO;
import com.qikserve.checkout.model.entities.cart.Cart;
import com.qikserve.checkout.model.entities.cart.CartItem;
import com.qikserve.checkout.model.entities.cart.CartSummary;
import com.qikserve.checkout.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart response = this.cartService.createCart();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId , @RequestParam(required = false) String filter) {
        Cart response = this.cartService.getCartById(cartId);

        if( filter != null && filter.equals("grouped")){
            Cart cart = this.cartService.groupItems(response);
            return ResponseEntity.ok(cart);
        }

        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> response = this.cartService.getAllCarts();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{cartId}/item")
    public ResponseEntity<List<CartItem>> addCartItems(@PathVariable Long cartId, @RequestBody List<CartItem> cartItems){
        List<CartItem> items = this.cartService.addCartItems(cartId, cartItems);
        return ResponseEntity.ok(items);
    }


    @DeleteMapping("/{cartId}/item/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long cartId, @PathVariable Long cartItemId){
        this.cartService.removeItem(cartId, cartItemId);

        return ResponseEntity.ok(messageSource.getMessage("cart.item.removed", new Object[]{cartItemId}, null));
    }

    @PostMapping("{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        this.cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{cartId}/cancel")
    public ResponseEntity<Void> cancelCart(@PathVariable Long cartId) {
        this.cartService.cancel(cartId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<CartSummary> checkout(@PathVariable Long cartId) {

        CartSummary response = this.cartService.checkout(cartId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cartId}/summary")
    public ResponseEntity<CartSummary> cartSummary(@PathVariable Long cartId) {
        CartSummary response = this.cartService.getCartSummaryByCartId(cartId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{cartId}/savings")
    public ResponseEntity<CartSavingsDTO> cartSavings(@PathVariable Long cartId) {
        CartSavingsDTO response = this.cartService.cartSavings(cartId);
        return ResponseEntity.ok(response);
    }

}