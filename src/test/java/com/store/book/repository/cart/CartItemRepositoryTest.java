package com.store.book.repository.cart;

import static org.assertj.core.api.Assertions.assertThat;

import com.store.book.model.CartItem;
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
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("Find cart item by shopping cart ID and book ID")
    @Sql(scripts = "classpath:database/shopping_carts/add-cart-items-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shopping_carts/clear-shopping-cart-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByShoppingCartIdAndBookId_ValidIds_ShouldReturnCartItem() {
        Long shoppingCartId = 100L;
        Long bookId = 100L;

        Optional<CartItem> actual =
                cartItemRepository.findByShoppingCartIdAndBookId(shoppingCartId, bookId);

        assertThat(actual).isPresent();
        assertThat(actual.get().getQuantity()).isEqualTo(5);
        assertThat(actual.get().getBook().getId()).isEqualTo(bookId);
    }

    @Test
    @DisplayName("Знайти товар у кошику за його власним ID та ID користувача")
    @Sql(scripts = {
            "classpath:database/shopping_carts/add-cart-items-data.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shopping_carts/clear-shopping-cart-data.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdAndShoppingCartId_ValidIds_ShouldReturnCartItem() {
        Long userId = 100L;
        Long bookId = 100L;

        CartItem expectedItem = cartItemRepository.findByShoppingCartIdAndBookId(userId, bookId)
                .orElseThrow(() -> new AssertionError("Product not found in "
                        + "the database before the test"));

        Long actualCartItemId = expectedItem.getId();

        Optional<CartItem> actual = cartItemRepository
                .findByIdAndShoppingCartId(actualCartItemId, userId);

        assertThat(actual).isPresent();
        assertThat(actual.get().getId()).isEqualTo(actualCartItemId);
        assertThat(actual.get().getShoppingCart().getUser().getId()).isEqualTo(userId);
    }
}
