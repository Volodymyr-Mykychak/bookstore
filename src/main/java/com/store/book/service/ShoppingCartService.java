package com.store.book.service;

import com.store.book.dto.cart.CartItemRequestDto;
import com.store.book.dto.cart.CartItemUpdateDto;
import com.store.book.dto.cart.ShoppingCartDto;
import com.store.book.model.User;

public interface ShoppingCartService {
    ShoppingCartDto findByUserId(Long userId);

    ShoppingCartDto addBookToCart(Long userId, CartItemRequestDto requestDto);

    ShoppingCartDto updateCartItem(Long userId, Long cartItemId, CartItemUpdateDto updateDto);

    void removeCartItem(Long userId, Long cartItemId);

    void createShoppingCartForUser(User user);
}
