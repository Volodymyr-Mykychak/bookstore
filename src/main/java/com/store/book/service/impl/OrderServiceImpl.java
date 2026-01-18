package com.store.book.service.impl;

import com.store.book.dto.order.CreateOrderRequestDto;
import com.store.book.dto.order.OrderDto;
import com.store.book.dto.order.OrderItemDto;
import com.store.book.dto.order.UpdateOrderStatusDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.exception.OrderProcessingException;
import com.store.book.mapper.OrderItemMapper;
import com.store.book.mapper.OrderMapper;
import com.store.book.model.Order;
import com.store.book.model.OrderItem;
import com.store.book.model.ShoppingCart;
import com.store.book.repository.UserRepository;
import com.store.book.repository.cart.ShoppingCartRepository;
import com.store.book.repository.order.OrderItemRepository;
import com.store.book.repository.order.OrderRepository;
import com.store.book.service.BookQuantityService;
import com.store.book.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final BookQuantityService bookQuantityService;

    @Override
    public Page<OrderDto> getAll(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderDto create(Long userId, CreateOrderRequestDto addressDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart for user " + userId));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Cannot place an order with an empty shopping cart");
        }
        Order order = orderMapper.toModel(addressDto);
        order.setUser(userRepository.getReferenceById(userId));
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream().map(cartItem -> {
            bookQuantityService.secureStockUpdate(cartItem.getBook().getId(),
                    -cartItem.getQuantity());
            OrderItem orderItem = orderItemMapper.fromCartToOrderItem(cartItem);
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        BigDecimal totalPrice = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(totalPrice);
        Order savedOrder = orderRepository.save(order);
        shoppingCart.getCartItems().clear();
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto updateStatus(Long orderId, UpdateOrderStatusDto updateDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + orderId));
        if (updateDto.getStatus() == Order.Status.CANCELLED
                && order.getStatus() != Order.Status.CANCELLED) {
            for (OrderItem item : order.getOrderItems()) {
                bookQuantityService.secureStockUpdate(item.getBook().getId(), item.getQuantity());
            }
        }
        order.setStatus(updateDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderItemDto> getAllItems(Long userId, Long orderId, Pageable pageable) {
        Page<OrderItem> orderItems =
                orderItemRepository.findAllByOrderUserIdAndOrderId(userId, orderId, pageable);
        return orderItems.map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemDto getItemById(Long userId, Long orderId, Long itemId) {
        return orderItemRepository.findByOrderUserIdAndOrderIdAndId(userId, orderId, itemId)
                .map(orderItemMapper::toDto).orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order item with id " + itemId + " for order " + orderId
                                + " and user " + userId));
    }

    @Override
    public OrderDto getById(Long userId, Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, userId).map(orderMapper::toDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    @Override
    public Page<OrderDto> getAllForAdmin(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }
}
