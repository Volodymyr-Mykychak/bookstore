package com.store.book.repository.book;

import com.store.book.model.Book;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {
    // старий метод (можна залишити, якщо потрібно)
    List<Book> findAllByUserId(Long userId);

    // новий метод з підтримкою пагінації
    Page<Book> findAllByUserId(Long userId, Pageable pageable);
}

