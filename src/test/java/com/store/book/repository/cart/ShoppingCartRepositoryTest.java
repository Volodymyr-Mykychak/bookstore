package com.store.book.repository.cart;

import static org.assertj.core.api.Assertions.assertThat;

import com.store.book.model.ShoppingCart;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find shopping cart by valid user ID")
    @Sql(scripts = {
            "classpath:database/shopping_carts/add-shopping-cart-data.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shopping_carts/clear-shopping-cart-data.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ValidUserId_ShouldReturnShoppingCart() {
        Long userId = 100L;

        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(userId);

        assertThat(actual).isPresent();
        assertThat(actual.get().getUser().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Find shopping cart by non-existing user ID")
    void findByUserId_InvalidUserId_ShouldReturnEmptyOptional() {
        Long userId = 999L;

        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(userId);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Find shopping cart: Should return empty if cart is soft-deleted")
    void findByUserId_SoftDeletedCart_ShouldReturnEmpty() {
        Long deletedUserId = 101L;

        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(deletedUserId);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Find shopping cart: Should contain cart items if they exist")
    @Sql(scripts = "classpath:database/shopping_carts/add-cart-items-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shopping_carts/clear-shopping-cart-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_WithItems_ShouldReturnCartWithItems() {
        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(100L);

        assertThat(actual).isPresent();
        assertThat(actual.get().getCartItems()).isNotEmpty();
    }
}
