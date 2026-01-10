package com.store.book.mapper;

import com.store.book.config.MapperConfig;
import com.store.book.dto.cart.CartItemResponseDto;
import com.store.book.dto.cart.ShoppingCartDto;
import com.store.book.model.CartItem;
import com.store.book.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", source = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
    
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toCartItemDto(CartItem cartItem);
}
