package com.store.book.mapper;

import com.store.book.config.MapperConfig;
import com.store.book.dto.order.CreateOrderRequestDto;
import com.store.book.dto.order.OrderDto;
import com.store.book.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "items")
    OrderDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Order toModel(CreateOrderRequestDto requestDto);
}

