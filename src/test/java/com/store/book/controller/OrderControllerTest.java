package com.store.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.book.dto.order.CreateOrderRequestDto;
import com.store.book.dto.order.OrderDto;
import com.store.book.dto.order.OrderItemDto;
import com.store.book.dto.order.UpdateOrderStatusDto;
import com.store.book.model.Order;
import com.store.book.service.OrderService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(
        scripts = {
                "classpath:database/orders/delete-user-for-orders.sql",
                "classpath:database/orders/add-user-for-orders.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = "classpath:database/orders/delete-user-for-orders.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails("order-user@example.com")
    @DisplayName("Get order history")
    void getAll_ValidUser_ShouldReturnPage() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setTotal(BigDecimal.TEN);
        when(orderService.getAll(anyLong(), any())).thenReturn(new PageImpl<>(List.of(orderDto)));

        mockMvc.perform(get("/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @Test
    @WithUserDetails("order-user@example.com")
    @DisplayName("Create order: Should return 201 Created")
    void createOrder_ValidRequest_ShouldReturnCreated() throws Exception {
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        requestDto.setShippingAddress("123 Street");
        OrderDto responseDto = new OrderDto();
        responseDto.setId(1L);
        when(orderService.create(anyLong(), any())).thenReturn(responseDto);

        mockMvc.perform(post("/orders")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("order-user@example.com")
    @DisplayName("Get order item: Should return specific item")
    void getOrderItemById_ValidRequest_ShouldReturnItem() throws Exception {
        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setId(1L);
        itemDto.setBookId(1L);
        when(orderService.getItemById(anyLong(), anyLong(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(get("/orders/1/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Update status: Regular user should be forbidden")
    void updateStatus_UserRole_ShouldReturnForbidden() throws Exception {
        UpdateOrderStatusDto updateDto = new UpdateOrderStatusDto();
        updateDto.setStatus(Order.Status.DELIVERED);

        mockMvc.perform(put("/orders/1")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
