package com.store.book.mapper;

import com.store.book.config.MapperConfig;
import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    Book toModel(CreateBookRequestDto requestDto);
}
