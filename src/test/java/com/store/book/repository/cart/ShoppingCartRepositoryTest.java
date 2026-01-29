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
@Sql(
        statements = {
                "DELETE FROM cart_items",
                "DELETE FROM shopping_carts",
                "DELETE FROM users_roles",
                "DELETE FROM users"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
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
        // Тут імейл має збігатися з тим, що в add-shopping-cart-data.sql
        assertThat(actual.get().getUser().getEmail()).isNotNull();
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
    @Sql(scripts = {
            "classpath:database/shopping_carts/add-shopping-cart-data.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shopping_carts/clear-shopping-cart-data.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_SoftDeletedCart_ShouldReturnEmpty() {
        // У вашій базі ID 101 має бути помічений як is_deleted = true
        Long deletedUserId = 101L;

        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(deletedUserId);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Find shopping cart: Should contain cart items if they exist")
    @Sql(scripts = {
            // МИ НЕ ДОДАЄМО add-shopping-cart-data.sql ТУТ,
            // бо add-cart-items-data.sql сам вставляє користувача 100
            "classpath:database/shopping_carts/add-cart-items-data.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shopping_carts/clear-shopping-cart-data.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_WithItems_ShouldReturnCartWithItems() {
        Long userId = 100L;
        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(userId);

        assertThat(actual).isPresent();
        assertThat(actual.get().getCartItems()).isNotEmpty();
    }
}
