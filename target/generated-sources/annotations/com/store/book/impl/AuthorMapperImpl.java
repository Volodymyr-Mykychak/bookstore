package com.store.book.impl;

import com.store.book.dto.author.AuthorDto;
import com.store.book.dto.author.CreateAuthorRequestDto;
import com.store.book.mapper.AuthorMapper;
import com.store.book.model.Author;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-21T16:20:34+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Ubuntu)"
)
@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public AuthorDto toDto(Author author) {
        if ( author == null ) {
            return null;
        }

        AuthorDto authorDto = new AuthorDto();

        if ( author.getId() != null ) {
            authorDto.setId( author.getId() );
        }
        if ( author.getName() != null ) {
            authorDto.setName( author.getName() );
        }
        if ( author.getBio() != null ) {
            authorDto.setBio( author.getBio() );
        }

        return authorDto;
    }

    @Override
    public Author toModel(CreateAuthorRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        Author author = new Author();

        if ( requestDto.name() != null ) {
            author.setName( requestDto.name() );
        }
        if ( requestDto.bio() != null ) {
            author.setBio( requestDto.bio() );
        }

        return author;
    }
}
