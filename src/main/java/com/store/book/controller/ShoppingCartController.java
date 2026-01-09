package com.store.book.controller;

import com.store.book.dto.cart.CartItemRequestDto;
import com.store.book.dto.cart.CartItemUpdateDto;
import com.store.book.dto.cart.ShoppingCartDto;
import com.store.book.model.User;
import com.store.book.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart", description = "Endpoints for managing the shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get current user's shopping cart")
    @GetMapping
    public ShoppingCartDto getInfo(@AuthenticationPrincipal User user) {
        return shoppingCartService.findByUserId(user.getId());
    }

    @Operation(summary = "Add book to cart",
            description = "Adds a book to the cart. If the book is already there, increases "
                          + "quantity.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartDto addBook(
            @Valid @RequestBody CartItemRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        return shoppingCartService.addBookToCart(user.getId(), requestDto);
    }

    @Operation(summary = "Update quantity of a book in the cart")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto updateQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemUpdateDto updateDto,
            @AuthenticationPrincipal User user) {
        return shoppingCartService.updateCartItem(user.getId(), cartItemId, updateDto);
    }

    @Operation(summary = "Remove a book from the cart")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal User user) {
        shoppingCartService.removeCartItem(user.getId(), cartItemId);
    }
}
