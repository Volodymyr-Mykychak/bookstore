package com.store.book.service;

import com.store.book.dto.order.CreateOrderRequestDto;
import com.store.book.dto.order.OrderDto;
import com.store.book.dto.order.OrderItemDto;
import com.store.book.dto.order.UpdateOrderStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getAll(Long userId, Pageable pageable);

    OrderDto create(Long userId, CreateOrderRequestDto address);

    OrderDto updateStatus(Long orderId, UpdateOrderStatusDto status);

    Page<OrderItemDto> getAllItems(Long userId, Long orderId, Pageable pageable);

    OrderItemDto getItemById(Long userId, Long orderId, Long itemId);

    OrderDto getById(Long userId, Long orderId);

    Page<OrderDto> getAllForAdmin(Pageable pageable);
}
