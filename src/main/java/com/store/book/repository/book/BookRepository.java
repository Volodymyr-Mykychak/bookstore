package com.store.book.repository.book;

import com.store.book.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findAllByCategoriesId(Long categoryId);

    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.quantity = b.quantity + :delta "
            + "WHERE b.id = :id AND (b.quantity + :delta) >= 0")
    int updateQuantity(Long id, int delta);
}
