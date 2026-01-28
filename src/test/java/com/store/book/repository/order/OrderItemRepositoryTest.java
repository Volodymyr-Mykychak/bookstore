package com.store.book.repository.order;

import com.store.book.model.OrderItem;
import java.util.List;
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
                "DELETE FROM order_items",
                "DELETE FROM orders",
                "DELETE FROM cart_items",
                "DELETE FROM shopping_carts",
                "DELETE FROM books_categories",
                "DELETE FROM books",
                "DELETE FROM categories"
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
@Sql(
        scripts = {
                "classpath:database/orders/delete-orders.sql",
                "classpath:database/books/delete-books.sql",
                "classpath:database/categories/delete-categories.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("Find all items by Order ID and User ID - Pagination")
    void findAllByOrderUserIdAndOrderId_ValidIds_ShouldReturnPageOfItems() {
        Long userId = 1L;
        Long orderId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<OrderItem> actual = orderItemRepository
                .findAllByOrderUserIdAndOrderId(userId, orderId, pageable);

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1, actual.getTotalElements());
    }

    @Test
    @DisplayName("Find specific item by User ID, Order ID and Item ID")
    void findByOrderUserIdAndOrderIdAndId_ValidIds_ShouldReturnItem() {
        Long userId = 1L;
        Long orderId = 1L;
        Long itemId = 1L;

        Optional<OrderItem> actual = orderItemRepository
                .findByOrderUserIdAndOrderIdAndId(userId, orderId, itemId);

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(itemId, actual.get().getId());
    }

    @Test
    @DisplayName("Find all items by Book ID")
    void findAllByBookId_ValidBookId_ShouldReturnList() {
        Long bookId = 1L;

        List<OrderItem> actual = orderItemRepository.findAllByBookId(bookId);

        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(bookId, actual.get(0).getBook().getId());
    }
}
