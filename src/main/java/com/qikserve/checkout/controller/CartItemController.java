package com.qikserve.checkout.controller;

import com.qikserve.checkout.model.entities.cart.Cart;
import com.qikserve.checkout.model.entities.cart.CartItem;
import com.qikserve.checkout.service.cart.item.ICartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartItem")
@RequiredArgsConstructor
public class CartItemController {

    private final ICartItemService cartItemService;

    @PostMapping
    public ResponseEntity<CartItem> createCartItem(@Valid @RequestBody CartItem cartItem) {
        CartItem response = this.cartItemService.createCartItem(cartItem);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItem> getCartItem(@PathVariable Long cartItemId) {
        CartItem response = this.cartItemService.getCartItemById(cartItemId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long cartItemId){
        this.cartItemService.removeCartItem(cartItemId);

        return ResponseEntity.ok("Item with id " + cartItemId + " removed");
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<String> updateQuantity(@PathVariable Long cartItemId, @RequestBody int quantity) {
        this.cartItemService.updateQuantityCartItem(cartItemId, quantity);
        return ResponseEntity.ok("Item with id " + cartItemId + " has updated quantity");
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getAllCarts() {
        List<CartItem> response = this.cartItemService.getAllCartItems();
        return ResponseEntity.ok(response);
    }

}