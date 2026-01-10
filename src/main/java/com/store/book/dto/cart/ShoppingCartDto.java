package com.store.book.dto.cart;

import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
