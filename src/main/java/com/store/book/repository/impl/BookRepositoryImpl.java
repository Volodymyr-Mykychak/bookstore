package com.store.book.repository.impl;

import com.store.book.model.Book;
import com.store.book.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Book save(Book book) {
        try {
            entityManager.persist(book);
            return book;
        } catch (Exception e) {
            throw new RuntimeException("Can't save book to DB", e);
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            return entityManager.createQuery(
                    "SELECT u FROM Book u", Book.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get all products from DB", e);
        }
    }
}
