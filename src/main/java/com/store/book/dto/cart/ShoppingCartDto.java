package com.store.book.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "10")
    private Long userId;
    @Schema(description = "Set of items currently in the shopping cart")
    private Set<CartItemResponseDto> cartItems;
}
