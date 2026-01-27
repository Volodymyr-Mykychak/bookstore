package com.store.book.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.book.dto.cart.CartItemRequestDto;
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
    @DisplayName("Get shopping cart: Should return user's cart")
    @Sql(scripts = {
            "classpath:database/shopping_carts/clear-carts.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/categories/add-fiction-category.sql",
            "classpath:database/books/add-effective-java-book.sql",
            "classpath:database/shopping_carts/add-cart-and-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getCart_AuthenticatedUser_ShouldReturnCartDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getCartItems()).isNotEmpty();
    }

    @Test
    @WithUserDetails("user@example.com")
    @DisplayName("Add item to cart: Valid request should return updated cart")
    @Sql(scripts = {
            "classpath:database/shopping_carts/clear-carts.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/categories/add-fiction-category.sql",
            "classpath:database/books/add-effective-java-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void addItem_ValidRequest_ShouldReturnCreatedStatus() throws Exception {
        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setBookId(1L);
        requestDto.setQuantity(2);

        mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("user@example.com")
    @DisplayName("Update item quantity: Should return updated cart")
    @Sql(scripts = {
            "classpath:database/shopping_carts/clear-carts.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/categories/add-fiction-category.sql",
            "classpath:database/books/add-effective-java-book.sql",
            "classpath:database/shopping_carts/add-cart-and-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateItem_ValidRequest_ShouldReturnUpdatedQuantity() throws Exception {
        Long cartItemId = 1L;
        CartItemUpdateDto updateDto = new CartItemUpdateDto();
        updateDto.setQuantity(5);

        mockMvc.perform(put("/cart/items/{cartItemId}", cartItemId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@example.com")
    @DisplayName("Delete item from cart: Should return no content status")
    @Sql(scripts = {
            "classpath:database/shopping_carts/clear-carts.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/categories/add-fiction-category.sql",
            "classpath:database/books/add-effective-java-book.sql",
            "classpath:database/shopping_carts/add-cart-and-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteItem_ValidId_ShouldReturnNoContent() throws Exception {
        Long cartItemId = 1L;

        mockMvc.perform(delete("/cart/items/{cartItemId}", cartItemId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
