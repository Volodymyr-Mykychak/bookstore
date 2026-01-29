package com.store.book.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.book.dto.cart.CartItemRequestDto;
import com.store.book.dto.cart.CartItemResponseDto;
import com.store.book.dto.cart.CartItemUpdateDto;
import com.store.book.dto.cart.ShoppingCartDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("user@example.com")
    @DisplayName("Add item to cart: Should return cart with new item")
    @Sql(scripts = {
            "classpath:database/shopping_carts/clear-carts.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/categories/add-fiction-category.sql",
            "classpath:database/books/add-effective-java-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void addItem_ValidRequest_ShouldReturnUpdatedCart() throws Exception {
        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setBookId(1L);
        requestDto.setQuantity(2);

        mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartItems[0].bookId").value(1L))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(2));
    }

    @Test
    @WithUserDetails("user@example.com")
    @DisplayName("Update quantity: Should change item quantity in DB")
    @Sql(scripts = {
            "classpath:database/shopping_carts/clear-carts.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/categories/add-fiction-category.sql",
            "classpath:database/books/add-effective-java-book.sql",
            "classpath:database/shopping_carts/add-cart-and-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateItem_ValidRequest_ShouldReturnUpdatedDto() throws Exception {
        CartItemUpdateDto updateDto = new CartItemUpdateDto();
        updateDto.setQuantity(10);

        MvcResult result = mockMvc.perform(put("/cart/items/1")
                        .content(objectMapper.writeValueAsBytes(updateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);

        CartItemResponseDto updatedItem = actual.getCartItems().stream()
                .filter(item -> item.getId().equals(1L))
                .findFirst().orElseThrow();
        assertThat(updatedItem.getQuantity()).isEqualTo(10);
    }

    @Test
    @WithUserDetails("user@example.com")
    @DisplayName("Add item: Should return 400 for invalid quantity")
    void addItem_InvalidQuantity_ShouldReturnBadRequest() throws Exception {
        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setBookId(1L);
        requestDto.setQuantity(-5);

        mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@example.com")
    @DisplayName("Update item: Should return 404 for non-existent item")
    void updateItem_NonExistentId_ShouldReturnNotFound() throws Exception {
        CartItemUpdateDto updateDto = new CartItemUpdateDto();
        updateDto.setQuantity(5);

        mockMvc.perform(put("/cart/items/999")
                        .content(objectMapper.writeValueAsBytes(updateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
