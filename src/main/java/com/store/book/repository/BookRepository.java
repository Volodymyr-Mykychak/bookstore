package com.store.book.repository;

import com.store.book.model.Book;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository {
    Book save(Book book);
    List<Book> findAll();
}
