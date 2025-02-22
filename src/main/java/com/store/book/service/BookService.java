package com.store.book.service;

import com.store.book.dto.BookDto;
import com.store.book.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    List<BookDto> findAllByTitle(String title);

    BookDto findById(Long id);

    void deleteById(Long id);

}
