package com.store.book.service;

import com.store.book.exception.OrderProcessingException;
import com.store.book.repository.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookQuantityService {
    private final BookRepository bookRepository;

    @Transactional
    public void secureStockUpdate(Long bookId, int delta) {
        int updatedRows = bookRepository.updateQuantity(bookId, delta);
        if (updatedRows == 0) {
            throw new OrderProcessingException(
                    "Not enough stock or book not found for ID: " + bookId);
        }
    }
}
