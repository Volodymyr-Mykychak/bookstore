package com.store.book.service.book.impl;

import java.util.List;
import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getById(Long id);

    List<BookDto> findAllByTitle(String title);

    List<BookDto> deleteById(Long id);

    List<BookDto> updateBook(Long id, CreateBookRequestDto bookDto);

}
