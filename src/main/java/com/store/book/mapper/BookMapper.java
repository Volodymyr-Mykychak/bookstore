package com.store.book.mapper;

import com.store.book.config.MapperConfig;
import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
