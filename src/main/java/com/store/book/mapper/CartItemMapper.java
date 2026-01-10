package com.store.book.mapper;

import com.store.book.config.MapperConfig;
import com.store.book.dto.cart.CartItemRequestDto;
import com.store.book.dto.cart.CartItemResponseDto;
import com.store.book.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    CartItem toModel(CartItemRequestDto requestDto);
}
