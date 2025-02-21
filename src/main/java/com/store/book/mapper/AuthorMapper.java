package com.store.book.mapper;

import com.store.book.config.MapperConfig;
import com.store.book.dto.author.AuthorDto;
import com.store.book.dto.author.CreateAuthorRequestDto;
import com.store.book.model.Author;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface AuthorMapper {
    AuthorDto toDto(Author author);

    Author toModel(CreateAuthorRequestDto requestDto);

    @Named("authorById")
    default Author authorById(Long id) {
        return Optional.ofNullable(id)
                .map(Author::new)
                .orElse(null);
    }
}
