package com.store.book.mapper;

import com.store.book.config.MapperConfig;
import com.store.book.dto.book.BookDto;
import com.store.book.dto.book.CreateBookRequestDto;
import com.store.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = AuthorMapper.class)
public interface BookMapper {

    @Mapping(source = "author.id", target = "authorId")
    BookDto toDto(Book book);

    @Mapping(target = "author", source = "authorId", qualifiedByName = "authorById")
    Book toModel(CreateBookRequestDto requestDto);

}
