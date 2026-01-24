package com.store.book.repository.book;

import static org.assertj.core.api.Assertions.assertThat;

import com.store.book.model.Book;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Find all books by category ID")
    @Sql(scripts = {"classpath:database/categories/add-fiction-category.sql",
                    "classpath:database/books/add-effective-java-book.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/delete-books.sql",
                    "classpath:database/categories/delete-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoriesId_ValidId_ReturnsBooks() {
        Long categoryId = 1L;
        List<Book> books = bookRepository.findAllByCategoriesId(categoryId);
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Effective Java");
    }

    @Test
    @DisplayName("Update book quantity: increment success")
    void updateQuantity_Increment_ReturnsOneAffectedRow() {
        Book book = createBook("Spring in Action", 10);
        entityManager.persist(book);
        entityManager.flush();
        int affectedRows = bookRepository.updateQuantity(book.getId(), 5);
        entityManager.clear();
        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(affectedRows).isEqualTo(1);
        assertThat(updatedBook.getQuantity()).isEqualTo(15);
    }

    @Test
    @DisplayName("Update book quantity: decrement success when balance is positive")
    void updateQuantity_Decrement_Success() {
        Book book = createBook("Clean Code", 10);
        entityManager.persist(book);
        entityManager.flush();
        int affectedRows = bookRepository.updateQuantity(book.getId(), -5);
        entityManager.clear();
        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(affectedRows).isEqualTo(1);
        assertThat(updatedBook.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("Update book quantity: should fail if resulting quantity < 0")
    void updateQuantity_DecrementBelowZero_NoChange() {
        Book book = createBook("Refactoring", 2);
        entityManager.persist(book);
        entityManager.flush();
        int affectedRows = bookRepository.updateQuantity(book.getId(), -5);
        entityManager.clear();
        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(affectedRows).isEqualTo(0);
        assertThat(updatedBook.getQuantity()).isEqualTo(2);
    }

    private Book createBook(String title, int quantity) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor("Author");
        book.setIsbn("978" + (int) (Math.random() * 1000000000));
        book.setPrice(BigDecimal.valueOf(100));
        book.setQuantity(quantity);
        return book;
    }
}
