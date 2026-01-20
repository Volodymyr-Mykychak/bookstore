package com.store.book.service.impl;

import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.BookSearchParametersDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.exception.OrderProcessingException;
import com.store.book.mapper.BookMapper;
import com.store.book.model.Book;
import com.store.book.repository.book.BookRepository;
import com.store.book.repository.book.BookSpecificationBuilder;
import com.store.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    @Transactional
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id " + id));
        bookMapper.updateBookFromDto(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find book to delete by id " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> spec = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(spec, pageable).map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public void updateStock(Long bookId, int quantityChange) {
        int updatedRows = bookRepository.updateQuantity(bookId, quantityChange);
        if (updatedRows == 0) {
            throw new OrderProcessingException(
                    "Not enough stock or book not found for ID: " + bookId);
        }
    }
}
