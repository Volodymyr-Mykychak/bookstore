package com.store.book.service.book.impl;

import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.exception.EntityNotFoundException;
import com.store.book.mapper.BookMapper;
import com.store.book.model.Book;
import com.store.book.repository.BookRepository;
import com.store.book.service.book.BookService;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toModel(bookDto);
        Random random = new Random();
        book.setIsbn(String.format("%03d-%010d", random.nextInt(1000),
                Math.abs(random.nextLong() % 1_000_000_0000L)));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {

        return bookRepository.findAll().stream()
                .map(bookMapper::toDto).toList();
    }

    @Override
    public List<BookDto> findAllByTitle(String title) {

        return bookRepository.findAllByTitleContainingIgnoreCase(title)
                .stream().map(bookMapper::toDto).toList();
    }

    @Override
    public BookDto findById(Long id) {

        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {

        bookRepository.findById(id).ifPresentOrElse(bookRepository::delete, () -> {
            throw new EntityNotFoundException("Can't delete, book with id "
                    + id + " not found.");
        });
    }
}
