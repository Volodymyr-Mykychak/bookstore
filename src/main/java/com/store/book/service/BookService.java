package com.store.book.service;

import com.store.book.dto.BookDto;
import com.store.book.dto.BookSearchParametersDto;
import com.store.book.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParametersDto);
}
