package com.qikserve.checkout.service.cart.item;

import com.qikserve.checkout.exception.cart.InvalidCartItemQuantityException;
import com.qikserve.checkout.exception.cart.notfound.CartItemNotFoundException;
import com.qikserve.checkout.model.entities.cart.*;
import com.qikserve.checkout.repository.cart.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements ICartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        CartItem cartItemBuilt = CartItem.builder()
                .quantity(cartItem.getQuantity())
                .build();
        return cartItemRepository.save(cartItemBuilt);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(()-> CartItemNotFoundException.of(cartItemId));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public CartItem getCartItemById(Long cartItemId) {

        return cartItemRepository.findById(cartItemId).orElseThrow(()-> CartItemNotFoundException.of(cartItemId));
    }

    @Override
    public void updateQuantityCartItem(Long cartItemId, int quantity) {
        if(quantity <= 0){
            throw InvalidCartItemQuantityException.of(cartItemId);
        }
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(()-> CartItemNotFoundException.of(cartItemId));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }
}