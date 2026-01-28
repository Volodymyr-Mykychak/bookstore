package com.store.book.repository.order;

import com.store.book.model.Order;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        statements = {
                "SET REFERENTIAL_INTEGRITY FALSE",
                "TRUNCATE TABLE order_items RESTART IDENTITY",
                "TRUNCATE TABLE orders RESTART IDENTITY",
                "TRUNCATE TABLE cart_items RESTART IDENTITY",
                "TRUNCATE TABLE shopping_carts RESTART IDENTITY",
                "TRUNCATE TABLE users_roles RESTART IDENTITY",
                "TRUNCATE TABLE users RESTART IDENTITY",
                "TRUNCATE TABLE books_categories RESTART IDENTITY",
                "TRUNCATE TABLE books RESTART IDENTITY",
                "TRUNCATE TABLE categories RESTART IDENTITY",
                "SET REFERENTIAL_INTEGRITY TRUE",
                "INSERT INTO users (id, email, password, first_name,"
                        + " last_name, shipping_address, is_deleted) "
                        + "VALUES (1, 'user@example.com', 'pass', 'John',"
                        + " 'Doe', 'Street 1', false)"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = {
                "classpath:database/categories/add-fiction-category.sql",
                "classpath:database/books/add-books.sql",
                "classpath:database/orders/add-orders.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Find all orders by user ID with pagination")
    void findAllByUserId_ValidUserId_ShouldReturnPageOfOrders() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<Order> actual = orderRepository.findAllByUserId(userId, pageable);

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1, actual.getTotalElements());
    }

    @Test
    @DisplayName("Find order by ID and User ID")
    void findByIdAndUserId_ValidIds_ShouldReturnOrder() {
        Long orderId = 1L;
        Long userId = 1L;

        Optional<Order> actual = orderRepository.findByIdAndUserId(orderId, userId);

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(orderId, actual.get().getId());
        Assertions.assertEquals(userId, actual.get().getUser().getId());
    }
}
