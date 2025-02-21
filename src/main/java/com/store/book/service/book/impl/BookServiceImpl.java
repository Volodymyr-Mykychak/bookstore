package com.store.book.service.book.impl;

import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.mapper.BookMapper;
import com.store.book.model.Book;
import com.store.book.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {

        Book book = bookMapper.toModel(bookDto);
        book.setIsbn("" + new Random().nextInt(1000));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {

        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {

        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find employee by id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAllByTitle(String title) {

        return bookRepository.findAllByNameContainingIgnoreCase(title)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> deleteById(Long id) {

        bookRepository.findById(id).ifPresentOrElse(
                bookRepository::delete,
                () -> {
                    throw new EntityNotFoundException("Can't delete, book with id " + id + " not found.");
                }
        );
    }

    @Override
    public List<BookDto> updateBook(Long id, CreateBookRequestDto bookDto) {

        return List.of();
    }
}

