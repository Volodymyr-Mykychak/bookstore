package com.store.book.service;

import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.BookSearchParametersDto;
import com.store.book.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);
}
