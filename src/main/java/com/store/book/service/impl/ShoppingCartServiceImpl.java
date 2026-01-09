package com.store.book.service.impl;

import com.store.book.dto.cart.CartItemRequestDto;
import com.store.book.dto.cart.CartItemUpdateDto;
import com.store.book.dto.cart.ShoppingCartDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.mapper.CartItemMapper;
import com.store.book.mapper.ShoppingCartMapper;
import com.store.book.model.CartItem;
import com.store.book.model.ShoppingCart;
import com.store.book.model.User;
import com.store.book.repository.book.BookRepository;
import com.store.book.repository.cart.CartItemRepository;
import com.store.book.repository.cart.ShoppingCartRepository;
import com.store.book.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto findByUserId(Long userId) {
        return shoppingCartMapper.toDto(getCartByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToCart(Long userId, CartItemRequestDto requestDto) {
        ShoppingCart cart = getCartByUserId(userId);
        var cartItemOpt = cartItemRepository
                .findByShoppingCartIdAndBookId(cart.getId(), requestDto.getBookId());
        if (cartItemOpt.isPresent()) {
            CartItem item = cartItemOpt.get();
            item.setQuantity(item.getQuantity() + requestDto.getQuantity());
        } else {
            createCartItem(cart, requestDto);
        }
        return shoppingCartMapper.toDto(cart);
    }

    private void createCartItem(ShoppingCart cart, CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(cart);
        cartItem.setBook(bookRepository.getReferenceById(requestDto.getBookId()));
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItem(Long userId, Long cartItemId,
                                          CartItemUpdateDto updateDto) {
        CartItem cartItem = getCartItemByIdAndUserId(userId, cartItemId);
        cartItem.setQuantity(updateDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(getCartByUserId(userId));
    }

    private CartItem getCartItemByIdAndUserId(Long cartItemId, Long userId) {
        return cartItemRepository.findByIdAndShoppingCartId(cartItemId, userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find cart item by id: " + cartItemId + " for " + "user: " + userId));
    }

    @Override
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = getCartItemByIdAndUserId(cartItemId, userId);
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart getCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart for user " + userId));
    }
}
