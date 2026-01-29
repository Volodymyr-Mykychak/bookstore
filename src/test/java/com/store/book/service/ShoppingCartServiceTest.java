package com.store.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.store.book.dto.cart.CartItemRequestDto;
import com.store.book.dto.cart.CartItemUpdateDto;
import com.store.book.dto.cart.ShoppingCartDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.mapper.ShoppingCartMapper;
import com.store.book.model.Book;
import com.store.book.model.CartItem;
import com.store.book.model.ShoppingCart;
import com.store.book.model.User;
import com.store.book.repository.book.BookRepository;
import com.store.book.repository.cart.CartItemRepository;
import com.store.book.repository.cart.ShoppingCartRepository;
import com.store.book.service.impl.ShoppingCartServiceImpl;
import com.store.book.util.TestDataHelper;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    private static final Long USER_ID = 1L;
    private static final Long CART_ITEM_ID = 1L;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Find by User ID: Should return valid DTO")
    void findByUserId_ValidId_ShouldReturnDto() {
        User user = TestDataHelper.getUser(USER_ID);
        ShoppingCart cart = TestDataHelper.getShoppingCart(user);
        ShoppingCartDto expectedDto = TestDataHelper.mapToDto(cart);

        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartDto actual = shoppingCartService.findByUserId(USER_ID);

        assertThat(actual).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Add book to cart: New item should be saved")
    void addBookToCart_NewItem_ShouldSaveAndReturnDto() {
        CartItemRequestDto requestDto = TestDataHelper.createCartItemRequestDto();
        User user = TestDataHelper.getUser(USER_ID);
        ShoppingCart cart = TestDataHelper.getShoppingCart(user);
        Book book = TestDataHelper.getBook(requestDto.getBookId());

        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(bookRepository.getReferenceById(requestDto.getBookId())).thenReturn(book);
        when(shoppingCartMapper.toDto(cart)).thenReturn(new ShoppingCartDto());

        shoppingCartService.addBookToCart(USER_ID, requestDto);

        verify(cartItemRepository).save(any(CartItem.class));
        assertThat(cart.getCartItems()).hasSize(1);
    }

    @Test
    @DisplayName("Update cart item: Should change quantity")
    void updateCartItem_ValidData_ShouldUpdateQuantity() {
        User user = TestDataHelper.getUser(USER_ID);
        ShoppingCart cart = TestDataHelper.getShoppingCart(user);
        CartItem cartItem = TestDataHelper.getCartItem(
                CART_ITEM_ID, cart, TestDataHelper.getBook(1L));

        when(cartItemRepository.findByIdAndShoppingCartId(CART_ITEM_ID, USER_ID))
                .thenReturn(Optional.of(cartItem));
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(new ShoppingCartDto());

        CartItemUpdateDto updateDto = TestDataHelper.createCartItemUpdateDto();
        shoppingCartService.updateCartItem(USER_ID, CART_ITEM_ID, updateDto);

        assertThat(cartItem.getQuantity()).isEqualTo(updateDto.getQuantity());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    @DisplayName("Remove cart item: Should call delete")
    void removeCartItem_ValidId_ShouldDelete() {
        CartItem cartItem = new CartItem();
        when(cartItemRepository.findByIdAndShoppingCartId(CART_ITEM_ID, USER_ID))
                .thenReturn(Optional.of(cartItem));

        shoppingCartService.removeCartItem(USER_ID, CART_ITEM_ID);

        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    @DisplayName("Get cart: Should throw EntityNotFoundException if cart missing")
    void findByUserId_InvalidId_ShouldThrowException() {
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.findByUserId(USER_ID));
    }

    @Test
    @DisplayName("Add book to cart: Should update quantity if item already exists")
    void addBookToCart_ExistingItem_ShouldUpdateQuantity() {
        CartItemRequestDto requestDto = TestDataHelper.createCartItemRequestDto();
        requestDto.setQuantity(3);

        User user = TestDataHelper.getUser(USER_ID);
        ShoppingCart cart = TestDataHelper.getShoppingCart(user);
        Book book = TestDataHelper.getBook(requestDto.getBookId());

        CartItem existingItem = TestDataHelper.getCartItem(CART_ITEM_ID, cart, book);
        existingItem.setQuantity(2);
        cart.getCartItems().add(existingItem);

        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(new ShoppingCartDto());

        shoppingCartService.addBookToCart(USER_ID, requestDto);

        assertThat(existingItem.getQuantity()).isEqualTo(5);
        verify(cartItemRepository).save(existingItem);
        assertThat(cart.getCartItems()).hasSize(1);
    }

    @Test
    @DisplayName("Create cart: Should save new shopping cart for user")
    void createShoppingCartForUser_ValidUser_ShouldSaveCart() {
        User user = TestDataHelper.getUser(USER_ID);

        shoppingCartService.createShoppingCartForUser(user);

        verify(shoppingCartRepository).save(any(ShoppingCart.class));
    }

    @Test
    @DisplayName("Remove cart item: Should throw exception if item not found")
    void removeCartItem_InvalidId_ShouldThrowException() {
        when(cartItemRepository.findByIdAndShoppingCartId(CART_ITEM_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.removeCartItem(USER_ID, CART_ITEM_ID));
    }

    @Test
    @DisplayName("Update cart item: Should throw exception if item not found")
    void updateCartItem_InvalidId_ShouldThrowException() {
        CartItemUpdateDto updateDto = new CartItemUpdateDto();

        when(cartItemRepository.findByIdAndShoppingCartId(CART_ITEM_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.updateCartItem(USER_ID, CART_ITEM_ID, updateDto));
    }
}
