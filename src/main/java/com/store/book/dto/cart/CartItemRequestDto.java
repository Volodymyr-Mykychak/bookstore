package com.store.book.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotNull(message = "Book ID cannot be null")
    @Positive(message = "Book ID must be positive")
    private Long bookId;
    @Positive(message = "Quantity must be at least 1")
    private int quantity;
}
