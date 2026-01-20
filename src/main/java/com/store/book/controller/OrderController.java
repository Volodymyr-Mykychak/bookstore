package com.store.book.controller;

import com.store.book.dto.order.CreateOrderRequestDto;
import com.store.book.dto.order.OrderDto;
import com.store.book.dto.order.OrderItemDto;
import com.store.book.dto.order.UpdateOrderStatusDto;
import com.store.book.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/orders")
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class OrderController implements UserContextHelper {
    private final OrderService orderService;

    @Operation(summary = "Get user order history",
            description = "Return list of all orders of " + "user")
    @GetMapping
    public Page<OrderDto> getAll(Authentication authentication,
            @ParameterObject @PageableDefault Pageable pageable) {
        return orderService.getAll(getUserId(authentication), pageable);
    }

    @Operation(summary = "Create order",
            description = "Create order from current shopping cart " + "items")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(Authentication authentication,
            @RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.create(getUserId(authentication), requestDto);
    }

    @Operation(summary = "Update order status",
            description = "Admin only: Updates the order " + "status")
    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto updateStatus(@PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderStatusDto updateDto) {
        return orderService.updateStatus(orderId, updateDto);
    }

    @Operation(summary = "Get all order items",
            description = "Get list of all items in specific " + "order")
    @GetMapping("/{orderId}/items")
    public Page<OrderItemDto> getAllOrderItems(Authentication authentication,
            @PathVariable Long orderId, @ParameterObject @PageableDefault Pageable pageable) {
        return orderService.getAllItems(getUserId(authentication), orderId, pageable);
    }

    @Operation(summary = "Get order item by id",
            description = "Get specific item details from " + "specific order")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemById(Authentication authentication, @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getItemById(getUserId(authentication), orderId, itemId);
    }

    @Operation(summary = "Get order details",
            description = "Get full information about a " + "specific order")
    @GetMapping("/{orderId}")
    public OrderDto getOrderById(Authentication authentication, @PathVariable Long orderId) {
        return orderService.getById(getUserId(authentication), orderId);
    }

    @Operation(summary = "Get all orders for admin",
            description = "Admin only: get all orders " + "in" + " system")
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderDto> getAllForAdmin(@ParameterObject @PageableDefault Pageable pageable) {
        return orderService.getAllForAdmin(pageable);
    }
}
