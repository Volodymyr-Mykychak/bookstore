package com.store.book.mapper;

import com.store.book.config.MapperConfig;
import com.store.book.dto.order.OrderItemDto;
import com.store.book.model.CartItem;
import com.store.book.model.OrderItem;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, imports = BigDecimal.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(source = "book.price", target = "price")
    OrderItem fromCartToOrderItem(CartItem cartItem);
}
