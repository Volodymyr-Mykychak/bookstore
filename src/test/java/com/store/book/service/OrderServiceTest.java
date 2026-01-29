package com.store.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.store.book.dto.order.CreateOrderRequestDto;
import com.store.book.dto.order.OrderDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.mapper.OrderItemMapper;
import com.store.book.mapper.OrderMapper;
import com.store.book.model.Book;
import com.store.book.model.CartItem;
import com.store.book.model.Order;
import com.store.book.model.OrderItem;
import com.store.book.model.ShoppingCart;
import com.store.book.model.User;
import com.store.book.repository.UserRepository;
import com.store.book.repository.cart.ShoppingCartRepository;
import com.store.book.repository.order.OrderRepository;
import com.store.book.service.impl.OrderServiceImpl;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private BookService bookService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Create order - Success")
    void create_ValidRequest_ShouldReturnOrderDto() {
        Long userId = 1L;
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        requestDto.setShippingAddress("Main St 123");

        User user = new User();
        user.setId(userId);

        Book book = new Book();
        book.setId(1L);
        book.setPrice(BigDecimal.valueOf(100));

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);

        Order order = new Order();
        order.setUser(user);

        OrderItem orderItem = new OrderItem();
        orderItem.setBook(book);
        orderItem.setPrice(book.getPrice());
        orderItem.setQuantity(cartItem.getQuantity());

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setTotal(BigDecimal.valueOf(200));

        OrderDto expectedDto = new OrderDto();
        expectedDto.setId(1L);
        expectedDto.setTotal(BigDecimal.valueOf(200));

        when(shoppingCartRepository.findById(userId)).thenReturn(Optional.of(shoppingCart));
        when(orderMapper.toModel(requestDto)).thenReturn(order);
        when(userRepository.getReferenceById(userId)).thenReturn(user);
        when(orderItemMapper.fromCartToOrderItem(cartItem)).thenReturn(orderItem);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toDto(savedOrder)).thenReturn(expectedDto);

        OrderDto actual = orderService.create(userId, requestDto);

        assertThat(actual).isEqualTo(expectedDto);
        verify(bookService).updateStock(book.getId(), -cartItem.getQuantity());
        verify(orderRepository).save(any(Order.class));
        assertThat(shoppingCart.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("Create order - Empty cart - Should throw exception")
    void create_EmptyCart_ShouldThrowException() {
        Long userId = 1L;
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        ShoppingCart emptyCart = new ShoppingCart();
        emptyCart.setCartItems(Collections.emptySet());

        when(shoppingCartRepository.findById(userId)).thenReturn(Optional.of(emptyCart));

        assertThrows(RuntimeException.class, () -> orderService.create(userId, requestDto));
    }

    @Test
    @DisplayName("Get order by ID - Existing ID - Should return OrderDto")
    void getById_ValidId_ShouldReturnOrderDto() {
        Long userId = 1L;
        Long orderId = 1L;
        Order order = new Order();
        OrderDto orderDto = new OrderDto();

        when(orderRepository.findByIdAndUserId(orderId, userId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        OrderDto actual = orderService.getById(userId, orderId);

        assertThat(actual).isEqualTo(orderDto);
    }

    @Test
    @DisplayName("Get order by ID - Non-existing ID - Should throw exception")
    void getById_InvalidId_ShouldThrowException() {
        Long userId = 1L;
        Long orderId = 999L;

        when(orderRepository.findByIdAndUserId(orderId, userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getById(userId, orderId));
    }
}
