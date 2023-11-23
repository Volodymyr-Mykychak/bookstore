package com.store.book.repository.impl;

import com.store.book.exception.BookRepositoryException;
import com.store.book.model.Book;
import com.store.book.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Book save(Book book) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new BookRepositoryException("Error saving book: " + book, e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Book book = entityManager.find(Book.class, id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            throw new BookRepositoryException("Error finding book with id: " + id, e);
        }
    }

    @Override
    public List<Book> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT e FROM Book e", Book.class).getResultList();
        } catch (Exception e) {
            throw new BookRepositoryException("Error retrieving all books", e);
        }
    }

    @Override
    public List<Book> findAllByTitle(String title) {
        String lowerCaseName = title.toLowerCase();
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("SELECT e FROM Book e WHERE lower(e.title) "
                            + "LIKE :title", Book.class)
                    .setParameter("title", "%" + lowerCaseName + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new BookRepositoryException("Error finding books with title: " + title, e);
        }
    }
}
