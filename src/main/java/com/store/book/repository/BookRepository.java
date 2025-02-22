package com.store.book.repository;

import com.store.book.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE UPPER(b.title) LIKE UPPER(:title)")
    List<Book> findAllByTitleContainingIgnoreCase(String title);

}
